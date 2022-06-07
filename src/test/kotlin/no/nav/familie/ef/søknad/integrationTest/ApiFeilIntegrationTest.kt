package no.nav.familie.ef.søknad.integrationTest

import no.nav.familie.ef.søknad.ApplicationLocalLauncher
import no.nav.security.token.support.core.api.Unprotected
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.exchange
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Profile("feil-controller")
@RestController
@Unprotected
@RequestMapping(path = ["/api/feil"], produces = [MediaType.APPLICATION_JSON_VALUE])
class FeilController {

    @GetMapping
    fun feil(): Unit = throw RuntimeException("Feil")
}

@ActiveProfiles("feil-controller")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [ApplicationLocalLauncher::class])
class ApiFeilIntegrationTest : OppslagSpringRunnerTest() {

    @BeforeEach
    fun førAlle() {
        headers.setBearerAuth(søkerBearerToken())
    }

    @Test
    fun `skal få 200 når autentisert og vi bruker get`() {

        val exchange: ResponseEntity<String> = restTemplate.exchange(localhost("/api/innlogget"),
                                                                     org.springframework.http.HttpMethod.GET,
                                                                     HttpEntity<Any>(headers))
        assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(exchange.body).isEqualTo("Autentisert kall")
    }

    @Test
    fun `skal få 404 når endepunkt ikke eksisterer`() {
        val exchange: ResponseEntity<String> = restTemplate.exchange(localhost("/api/finnesIkke"),
                                                                     org.springframework.http.HttpMethod.GET,
                                                                     HttpEntity<Any>(headers))
        assertThat(exchange.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    fun `skal få 500 når endepunkt kaster feil`() {
        val exchange: ResponseEntity<String> = restTemplate.exchange(localhost("/api/feil"),
                                                                     org.springframework.http.HttpMethod.GET,
                                                                     HttpEntity<Any>(headers))
        assertThat(exchange.statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
    }

}
