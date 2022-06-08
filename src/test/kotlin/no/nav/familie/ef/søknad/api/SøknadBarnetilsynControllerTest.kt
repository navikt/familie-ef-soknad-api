package no.nav.familie.ef.søknad.api

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Person
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadBarnetilsynDto
import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
import no.nav.familie.ef.søknad.integrationTest.OppslagSpringRunnerTest
import no.nav.familie.ef.søknad.mock.søkerMedDefaultVerdier
import no.nav.familie.ef.søknad.service.SøknadService
import no.nav.familie.kontrakter.felles.objectMapper
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
import org.springframework.test.context.ActiveProfiles
import java.io.File
import java.time.LocalDateTime

@Profile("overgangsstonad-controller-test")
@Configuration
class SøknadBarnetilsynControllerTestConfiguration {

    @Primary
    @Bean
    fun søknadService(): SøknadService = mockk()

    @Primary
    @Bean
    fun featureToggleService(): FeatureToggleService = mockk()
}

@ActiveProfiles("overgangsstonad-controller-test")
internal class SøknadBarnetilsynControllerTest : OppslagSpringRunnerTest() {

    @Autowired
    lateinit var søknadService: SøknadService

    @Autowired
    lateinit var featureToggleService: FeatureToggleService

    val tokenSubject = "12345678911"

    @BeforeEach
    fun førAlle() {
        headers.setBearerAuth(søkerBearerToken(tokenSubject))
    }

    fun søknadBarnetilsynDto(): SøknadBarnetilsynDto = objectMapper
        .readValue(
            File("src/test/resources/barnetilsyn/Barnetilsynsøknad.json"),
            SøknadBarnetilsynDto::class.java
        )

    @Test
    fun `sendInn returnerer kvittering riktig kvittering med riktig Bearer token`() {

        val søknad = søknadBarnetilsynDto()
            .copy(person = Person(søker = søkerMedDefaultVerdier(forventetFnr = tokenSubject), barn = listOf()))

        every { søknadService.sendInn(søknad, any()) } returns Kvittering(
            "Mottatt søknad: $søknad",
            LocalDateTime.now()
        )
        every { featureToggleService.isEnabled(any()) } returns true

        val response = restTemplate.exchange<Kvittering>(
            localhost("/api/soknad/barnetilsyn/"),
            HttpMethod.POST,
            HttpEntity(søknad, headers)
        )

        assertThat(response.statusCodeValue).isEqualTo(200)
        assertThat(response.body.text).isEqualTo("ok")
    }

    @Test
    fun `sendInn returnerer 403 ved ulik fnr i token og søknad`() {
        val søknadBarnetilsynDto = søknadBarnetilsynDto()

        val response = restTemplate.exchange<Any>(
            localhost("/api/soknad/barnetilsyn/"),
            HttpMethod.POST,
            HttpEntity(søknadBarnetilsynDto, headers)
        )

        assertThat(response.statusCodeValue).isEqualTo(403)
        verify(exactly = 0) { søknadService.sendInn(søknadBarnetilsynDto, any()) }
    }
}
