package no.nav.familie.ef.søknad.person

import no.nav.familie.ef.søknad.infrastruktur.OppslagSpringRunnerTest
import no.nav.familie.util.FnrGenerator
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpEntity

class OppslagControllerIntegrationTest : OppslagSpringRunnerTest() {
    val tokenSubject = FnrGenerator.generer()

    @BeforeEach
    fun førAlle() {
        headers.setBearerAuth(søkerBearerToken(tokenSubject))
    }

    @Test
    fun `Få response uten _navn fra sokerinfo endepunkt - skal feile hvis noen fjerner private fra _navn`() {
        val response =
            restTemplate.exchange<String>(
                localhost("/api/oppslag/sokerinfo"),
                org.springframework.http.HttpMethod.GET,
                HttpEntity<String>(headers),
            )
        assertThat(response.body).contains(tokenSubject) // guard
        assertThat(response.body).doesNotContain("_navn")
    }

//    @Test
//    fun `Skal bare ha en av hver CORS-header etter et kall`() {
//        headers.origin = "http://localhost:3000"
//        val response =
//            restTemplate.exchange<String>(
//                localhost("/api/oppslag/sokerinfo"),
//                org.springframework.http.HttpMethod.GET,
//                HttpEntity<String>(headers),
//            )
//
//        assertThat(response.headers["Access-Control-Allow-Origin"]).hasSize(1)
//        assertThat(response.headers["Access-Control-Allow-Headers"]).hasSize(1)
//        assertThat(response.headers["Access-Control-Allow-Credentials"]).hasSize(1)
//        assertThat(response.headers["Access-Control-Allow-Methods"]).hasSize(1)
//    }
}
