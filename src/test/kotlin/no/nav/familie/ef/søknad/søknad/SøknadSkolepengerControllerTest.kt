package no.nav.familie.ef.søknad.søknad

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import no.nav.familie.ef.søknad.infrastruktur.OppslagSpringRunnerTest
import no.nav.familie.ef.søknad.mock.søkerMedDefaultVerdier
import no.nav.familie.ef.søknad.søknad.domain.Kvittering
import no.nav.familie.ef.søknad.søknad.domain.Person
import no.nav.familie.ef.søknad.søknad.dto.SøknadSkolepengerDto
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
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import java.io.File
import java.time.LocalDateTime

@Profile("overgangsstonad-controller-test")
@Configuration
class SøknadSkolepengerControllerTestConfiguration {
    @Primary
    @Bean
    fun søknadService(): SøknadService = mockk()

}

@ActiveProfiles("overgangsstonad-controller-test")
internal class SøknadSkolepengerControllerTest : OppslagSpringRunnerTest() {
    @Autowired
    lateinit var søknadService: SøknadService

    val tokenSubject = "12345678911"

    @BeforeEach
    fun førAlle() {
        headers.setBearerAuth(søkerBearerToken(tokenSubject))
    }

    fun søknadSkolepenger() =
        objectMapper.readValue(
            File("src/test/resources/skolepenger/skolepenger.json"),
            SøknadSkolepengerDto::class.java,
        )

    @Test
    fun `sendInn returnerer kvittering riktig kvittering med riktig Bearer token`() {
        val søknad =
            søknadSkolepenger()
                .copy(
                    person =
                        Person(
                            søker = søkerMedDefaultVerdier(forventetFnr = tokenSubject),
                            barn = listOf(),
                        ),
                )

        every { søknadService.sendInn(søknad, any()) } returns
            Kvittering(
                "Mottatt søknad: $søknad",
                LocalDateTime.now(),
            )
        every { featureToggleService.isEnabled(any()) } returns true

        val response =
            restTemplate.exchange<Kvittering>(
                localhost("/api/soknad/skolepenger"),
                POST,
                HttpEntity(søknad, headers),
            )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.text).isEqualTo("ok")
    }

    @Test
    fun `sendInn returnerer 403 ved ulik fnr i token og søknad`() {
        val søknadSkolepengerDto = søknadSkolepenger()
        // guard
        assertThat(søknadSkolepengerDto.person.søker.fnr).isNotEqualTo(tokenSubject)

        val response =
            restTemplate.exchange<Any>(
                localhost("/api/soknad/skolepenger"),
                POST,
                HttpEntity(søknadSkolepengerDto, headers),
            )

        assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
        verify(exactly = 0) { søknadService.sendInn(søknadSkolepengerDto, any()) }
    }
}
