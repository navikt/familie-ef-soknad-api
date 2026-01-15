package no.nav.familie.ef.søknad.søknad

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import no.nav.familie.ef.søknad.infrastruktur.OppslagSpringRunnerTest
import no.nav.familie.ef.søknad.mock.søkerMedDefaultVerdier
import no.nav.familie.ef.søknad.mock.søknadBarnetilsynDto
import no.nav.familie.ef.søknad.mock.søknadOvergangsstønadDto
import no.nav.familie.ef.søknad.mock.søknadSkolepengerDto
import no.nav.familie.ef.søknad.søknad.domain.Kvittering
import no.nav.familie.ef.søknad.søknad.domain.Person
import no.nav.familie.ef.søknad.søknad.dto.SøknadBarnetilsynGjenbrukDto
import no.nav.familie.ef.søknad.søknad.mapper.SøknadBarnetilsynMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.exchange
import java.time.LocalDateTime

class SøknadControllerTest {
    @Profile("soknad-controller-test")
    @Configuration
    class SøknadControllerTestConfiguration {
        @Primary
        @Bean
        fun søknadService(): SøknadService = mockk()
    }

    @Nested
    @ActiveProfiles("soknad-controller-test")
    internal inner class SøknadControllerTest : OppslagSpringRunnerTest() {
        @Autowired
        lateinit var søknadService: SøknadService

        val tokenSubject = "12345678911"

        @BeforeEach
        fun førAlle() {
            headers.setBearerAuth(søkerBearerToken(tokenSubject))
        }

        @Test
        fun `innsending av søknad for overgangsstønad skal returnere kvittering med riktig Bearer token`() {
            val søknad = søknadOvergangsstønadDto().copy(person = Person(søker = søkerMedDefaultVerdier(forventetFnr = tokenSubject), barn = listOf()))
            every { søknadService.sendInnSøknadOvergangsstønad(søknad, any()) } returns
                Kvittering(
                    "Mottatt søknad: $søknad",
                    LocalDateTime.now(),
                )

            val response =
                restTemplate.exchange<Kvittering>(
                    localhost("/api/soknad/overgangsstonad"),
                    POST,
                    HttpEntity(søknad, headers),
                )

            assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(response.body?.text).isEqualTo("ok")
        }

        @Test
        fun `innsending av overgangsstønad skal returnere 403 ved ulik fnr i token og søknad`() {
            val søknad = søknadOvergangsstønadDto()
            assertThat(søknad.person.søker.fnr).isNotEqualTo(tokenSubject)

            val response =
                assertThrows<HttpClientErrorException.Forbidden> {
                    restTemplate.exchange<Any>(
                        localhost("/api/soknad/overgangsstonad"),
                        POST,
                        HttpEntity(søknad, headers),
                    )
                }
            assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
            verify(exactly = 0) { søknadService.sendInnSøknadOvergangsstønad(søknad, any()) }
        }

        @Test
        fun `innsending av søknad for barnetilsyn skal returnere kvittering med riktig Bearer token`() {
            val søknad =
                søknadBarnetilsynDto()
                    .copy(person = Person(søker = søkerMedDefaultVerdier(forventetFnr = tokenSubject), barn = listOf()))

            every { søknadService.sendInnSøknadBarnetilsyn(søknad, any()) } returns
                Kvittering(
                    "Mottatt søknad: $søknad",
                    LocalDateTime.now(),
                )

            val response =
                restTemplate.exchange<Kvittering>(
                    localhost("/api/soknad/barnetilsyn"),
                    POST,
                    HttpEntity(søknad, headers),
                )

            assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(response.body?.text).isEqualTo("ok")
        }

        @Test
        fun `innsending av søknad for barnetilsyn skal returnere 403 ved ulik fnr i token og søknad`() {
            val søknad = søknadBarnetilsynDto()
            assertThat(søknad.person.søker.fnr).isNotEqualTo(tokenSubject)

            val response =
                assertThrows<HttpClientErrorException.Forbidden> {
                    restTemplate.exchange<Any>(
                        localhost("/api/soknad/barnetilsyn"),
                        POST,
                        HttpEntity(søknad, headers),
                    )
                }

            assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
            verify(exactly = 0) { søknadService.sendInnSøknadBarnetilsyn(søknad, any()) }
        }

        @Test
        fun `skal returnere 200 dersom forrige barnetilsyn søknad ikke finnes`() {
            val søknad = søknadBarnetilsynDto().copy(person = Person(søker = søkerMedDefaultVerdier(forventetFnr = tokenSubject), barn = listOf()))

            every { søknadService.hentForrigeBarnetilsynSøknadKvittering() } returns null
            every { søknadService.harSøknadGyldigeVerdier(any()) } returns false
            val response =
                restTemplate.exchange<String>(localhost("/api/soknad/barnetilsyn/forrige"), GET, HttpEntity(søknad, headers))

            assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
            assertThat(response.body).isNull()
        }

        @Test
        fun `skal returnere forrige barnetilsyn søknad når den finnes`() {
            val søknad = søknadBarnetilsynDto().copy(person = Person(søker = søkerMedDefaultVerdier(forventetFnr = tokenSubject), barn = listOf()))
            val søknadMedVedlegg = SøknadBarnetilsynMapper().mapTilIntern(søknad, LocalDateTime.now(), true)
            every { søknadService.hentForrigeBarnetilsynSøknadKvittering() } returns SøknadBarnetilsynMapper().mapTilDto(søknadMedVedlegg.søknad)
            every { søknadService.harSøknadGyldigeVerdier(any()) } returns true
            val response =
                restTemplate.exchange<SøknadBarnetilsynGjenbrukDto>(
                    localhost("/api/soknad/barnetilsyn/forrige"),
                    GET,
                    HttpEntity<String>(headers),
                )
            assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        }

        @Test
        fun `innsending av søknad for skolepenger skal returnere kvittering med riktig Bearer token`() {
            val søknad =
                søknadSkolepengerDto()
                    .copy(
                        person =
                            Person(
                                søker = søkerMedDefaultVerdier(forventetFnr = tokenSubject),
                                barn = listOf(),
                            ),
                    )

            every { søknadService.sendInnSøknadSkolepenger(søknad, any()) } returns
                Kvittering(
                    "Mottatt søknad: $søknad",
                    LocalDateTime.now(),
                )

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
        fun `innsending av søknad for skolepenger skal returnere 403 ved ulik fnr i token og søknad`() {
            val søknad = søknadSkolepengerDto()
            assertThat(søknad.person.søker.fnr).isNotEqualTo(tokenSubject)

            val response =
                assertThrows<HttpClientErrorException.Forbidden> {
                    restTemplate.exchange<Any>(
                        localhost("/api/soknad/skolepenger"),
                        POST,
                        HttpEntity(søknad, headers),
                    )
                }

            assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
            verify(exactly = 0) { søknadService.sendInnSøknadSkolepenger(søknad, any()) }
        }
    }
}
