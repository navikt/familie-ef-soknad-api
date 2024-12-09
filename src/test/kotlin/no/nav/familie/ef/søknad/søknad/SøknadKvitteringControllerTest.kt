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
import no.nav.familie.ef.søknad.søknad.dto.SøknadBarnetilsynDto
import no.nav.familie.ef.søknad.søknad.dto.SøknadSkolepengerDto
import no.nav.familie.kontrakter.felles.objectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.exchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import java.io.File
import java.time.LocalDateTime
import kotlin.test.assertTrue

class SøknadKvitteringControllerTest {
    @Profile("soknad-controller-test")
    @Configuration
    class SøknadControllerTestConfiguration {
        @Primary
        @Bean
        fun søknadService(): SøknadService = mockk()

        @Primary
        @Bean
        fun featureToggleService(): FeatureToggleService = mockk()
    }

    @Nested
    @ActiveProfiles("soknad-controller-test")
    internal inner class SøknadKvitteringControllerTest : OppslagSpringRunnerTest() {
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
        fun `overgangsstønad sendInn returnerer riktig kvittering med riktig Bearer token`() {
            val søknad = søknadOvergangsstønadDto(tokenSubject)
            every { søknadService.sendInnSøknadskvittering(søknad, any()) } returns
                Kvittering(
                    "Mottatt søknad: $søknad",
                    LocalDateTime.now(),
                )
            every { featureToggleService.isEnabled(any()) } returns true

            val response =
                restTemplate.exchange<Kvittering>(
                    localhost("/api/soknadskvittering/overgangsstonad"),
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
        fun `overgangsstønad sendInn returnerer 403 ved ulik fnr i token og søknad`() {
            val søknad = søknadDto()

            val response =
                restTemplate.exchange<Any>(
                    localhost("/api/soknadskvittering/overgangsstonad"),
                    HttpMethod.POST,
                    HttpEntity(søknad, headers),
                )

            assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
            verify(exactly = 0) { søknadService.sendInn(søknad, any()) }
        }

        fun søknadBarnetilsynDto(): SøknadBarnetilsynDto =
            objectMapper
                .readValue(
                    File("src/test/resources/barnetilsyn/Barnetilsynsøknad.json"),
                    SøknadBarnetilsynDto::class.java,
                )

        @Test
        fun `barnetilsyn sendInn returnerer kvittering riktig kvittering med riktig Bearer token`() {
            val søknad =
                søknadBarnetilsynDto()
                    .copy(person = Person(søker = søkerMedDefaultVerdier(forventetFnr = tokenSubject), barn = listOf()))

            every { søknadService.sendInnSøknadskvitteringBarnetilsyn(søknad, any()) } returns
                Kvittering(
                    "Mottatt søknad: $søknad",
                    LocalDateTime.now(),
                )
            every { featureToggleService.isEnabled(any()) } returns true

            val response =
                restTemplate.exchange<Kvittering>(
                    localhost("/api/soknadskvittering/barnetilsyn"),
                    HttpMethod.POST,
                    HttpEntity(søknad, headers),
                )

            assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(response.body?.text).isEqualTo("ok")
        }

        @Test
        fun `barnetilsyn sendInn returnerer 403 ved ulik fnr i token og søknad`() {
            val søknadBarnetilsynDto = søknadBarnetilsynDto()

            val response =
                restTemplate.exchange<Any>(
                    localhost("/api/soknadskvittering/barnetilsyn"),
                    HttpMethod.POST,
                    HttpEntity(søknadBarnetilsynDto, headers),
                )

            assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
            verify(exactly = 0) { søknadService.sendInnSøknadskvitteringBarnetilsyn(søknadBarnetilsynDto, any()) }
        }

        fun søknadSkolepenger() =
            objectMapper.readValue(
                File("src/test/resources/skolepenger/skolepenger.json"),
                SøknadSkolepengerDto::class.java,
            )

        @Test
        fun `skolepenger sendInn returnerer kvittering riktig kvittering med riktig Bearer token`() {
            val søknad =
                søknadSkolepenger()
                    .copy(
                        person =
                            Person(
                                søker = søkerMedDefaultVerdier(forventetFnr = tokenSubject),
                                barn = listOf(),
                            ),
                    )

            every { søknadService.sendInnSøknadskvitteringSkolepenger(søknad, any()) } returns
                Kvittering(
                    "Mottatt søknad: $søknad",
                    LocalDateTime.now(),
                )
            every { featureToggleService.isEnabled(any()) } returns true

            val response =
                restTemplate.exchange<Kvittering>(
                    localhost("/api/soknadskvittering/skolepenger"),
                    POST,
                    HttpEntity(søknad, headers),
                )

            assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(response.body?.text).isEqualTo("ok")
        }

        @Test
        fun `skolepenger sendInn returnerer 403 ved ulik fnr i token og søknad`() {
            val søknadSkolepengerDto = søknadSkolepenger()
            // guard
            assertThat(søknadSkolepengerDto.person.søker.fnr).isNotEqualTo(tokenSubject)

            val response =
                restTemplate.exchange<Any>(
                    localhost("/api/soknadskvittering/skolepenger"),
                    POST,
                    HttpEntity(søknadSkolepengerDto, headers),
                )

            assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
            verify(exactly = 0) { søknadService.sendInnSøknadskvitteringSkolepenger(søknadSkolepengerDto, any()) }
        }

        @Test
        fun `mottak returnerer pdf kvittering`() {
            every { søknadService.hentSøknadPdf("1") } returns "pdf".toByteArray()

            val response =
                restTemplate.exchange<ByteArray>(
                    localhost("/api/soknadskvittering/1"),
                    HttpMethod.GET,
                    HttpEntity(null, headers),
                )
            assertTrue { response.body?.contentEquals("pdf".toByteArray()) ?: false }
        }
    }
}
