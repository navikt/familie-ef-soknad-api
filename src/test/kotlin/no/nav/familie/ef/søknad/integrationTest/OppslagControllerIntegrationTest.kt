package no.nav.familie.ef.søknad.integrationTest

import no.nav.familie.ef.søknad.ApplicationLocalLauncher
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpEntity

class OppslagControllerIntegrationTest : OppslagSpringRunnerTest() {

    val tokenSubject = "12345678911"

    @BeforeEach
    fun førAlle() {
        headers.setBearerAuth(søkerBearerToken(tokenSubject))
    }

    @Test
    fun `Få response uten _navn fra sokerinfo endepunkt - skal feile hvis noen fjerner private fra _navn`() {
        val response = restTemplate.exchange<String>(localhost("/api/oppslag/sokerinfo"),
                                                     org.springframework.http.HttpMethod.GET,
                                                     HttpEntity<String>(headers))
        assertThat(response.body).contains(tokenSubject) // guard
        assertThat(response.body).doesNotContain("_navn")
    }

}
