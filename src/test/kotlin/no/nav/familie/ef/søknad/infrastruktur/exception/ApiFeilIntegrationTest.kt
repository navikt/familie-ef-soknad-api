package no.nav.familie.ef.søknad.infrastruktur.exception

import no.nav.familie.ef.søknad.infrastruktur.OppslagSpringRunnerTest
import no.nav.security.token.support.core.api.Unprotected
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.exchange

@Profile("feil-controller")
@RestController
@Unprotected
@RequestMapping(path = ["/api/feil"], produces = [MediaType.APPLICATION_JSON_VALUE])
class FeilController {
    @GetMapping
    fun feil(): Unit = throw RuntimeException("Feil")
}

@ActiveProfiles("feil-controller")
class ApiFeilIntegrationTest : OppslagSpringRunnerTest() {
    @BeforeEach
    fun førAlle() {
        headers.setBearerAuth(søkerBearerToken())
    }

    @Test
    fun `skal få 200 når autentisert og vi bruker get`() {
        val exchange: ResponseEntity<String> =
            restTemplate.exchange(
                localhost("/api/innlogget"),
                HttpMethod.GET,
                HttpEntity<Any>(headers),
                String::class.java,
            )
        assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(exchange.body).isEqualTo("Autentisert kall")
    }

    @Test
    fun `skal få 404 når endepunkt ikke eksisterer`() {
        val exception =
            assertThrows<HttpClientErrorException.NotFound> {
                restTemplate.exchange<ResponseEntity<String>>(
                    localhost("/api/finnesIkke"),
                    HttpMethod.GET,
                    HttpEntity<Any>(headers),
                )
            }
        assertThat(exception.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `skal få 500 når endepunkt kaster feil`() {
        val exception =
            assertThrows<HttpServerErrorException.InternalServerError> {
                restTemplate.exchange<ResponseEntity<String>>(
                    localhost("/api/feil"),
                    HttpMethod.GET,
                    HttpEntity<Any>(headers),
                )
            }
        assertThat(exception.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
