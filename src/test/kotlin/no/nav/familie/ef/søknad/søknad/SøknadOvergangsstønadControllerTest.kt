package no.nav.familie.ef.søknad.søknad

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import no.nav.familie.ef.søknad.infrastruktur.OppslagSpringRunnerTest
import no.nav.familie.ef.søknad.infrastruktur.featuretoggle.FeatureToggleService
import no.nav.familie.ef.søknad.mock.søkerMedDefaultVerdier
import no.nav.familie.ef.søknad.mock.søknadDto
import no.nav.familie.ef.søknad.søknad.domain.Kvittering
import no.nav.familie.ef.søknad.søknad.domain.Person
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.exchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@Profile("overgangsstonad-controller-test")
@Configuration
class SøknadOvergangsstønadControllerTestConfiguration {
    @Primary
    @Bean
    fun søknadService(): SøknadService = mockk()

    @Primary
    @Bean
    fun featureToggleService(): FeatureToggleService = mockk()
}

@ActiveProfiles("overgangsstonad-controller-test")
internal class SøknadOvergangsstønadControllerTest : OppslagSpringRunnerTest() {
    @Autowired
    lateinit var søknadService: SøknadService

    @Autowired
    lateinit var featureToggleService: FeatureToggleService

    val tokenSubject = "12345678911"

    @BeforeEach
    fun førAlle() {
        headers.setBearerAuth(søkerBearerToken(tokenSubject))
    }

    @Test
    fun `sendInn returnerer kvittering riktig kvittering med riktig Bearer token`() {
        val søknad = søknadOvergangsstønadDto(tokenSubject)
        every { søknadService.sendInn(søknad, any()) } returns
            Kvittering(
                "Mottatt søknad: $søknad",
                LocalDateTime.now(),
            )
        every { featureToggleService.isEnabled(any()) } returns true

        val response =
            restTemplate.exchange<Kvittering>(
                localhost("/api/soknad/overgangsstonad"),
                HttpMethod.POST,
                HttpEntity(søknad, headers),
            )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.text).isEqualTo("ok")
    }

    private fun søknadOvergangsstønadDto(fnr: String) =
        søknadDto()
            .copy(person = Person(søker = søkerMedDefaultVerdier(forventetFnr = fnr), barn = listOf()))

    @Test
    fun `sendInn returnerer 403 ved ulik fnr i token og søknad`() {
        val søknad = søknadDto()

        val response =
            restTemplate.exchange<Any>(
                localhost("/api/soknad/overgangsstonad"),
                HttpMethod.POST,
                HttpEntity(søknad, headers),
            )

        assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
        verify(exactly = 0) { søknadService.sendInn(søknad, any()) }
    }
}
