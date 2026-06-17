package no.nav.familie.ef.søknad.person

import no.nav.familie.ef.søknad.infrastruktur.OppslagSpringRunnerTest
import no.nav.familie.ef.søknad.kodeverk.Landkode
import no.nav.familie.ef.søknad.person.domain.Søkerinfo
import no.nav.familie.kontrakter.felles.jsonMapper
import no.nav.familie.util.FnrGenerator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.exchange

class OppslagControllerIntegrationTest : OppslagSpringRunnerTest() {
    val tokenSubject = FnrGenerator.generer()

    @BeforeEach
    fun førAlle() {
        headers.setBearerAuth(søkerBearerToken(tokenSubject))
    }

    @Test
    fun `Få response uten _navn fra sokerinfo endepunkt - skal feile hvis noen fjerner private fra _navn`() {
        val response =
            restTemplate.exchange<Søkerinfo>(
                localhost("/api/oppslag/sokerinfo"),
                org.springframework.http.HttpMethod.GET,
                HttpEntity<String>(headers),
            )

        assertThat(jsonMapper.writeValueAsString(response)).contains(tokenSubject) // guard
        assertThat(jsonMapper.writeValueAsString(response)).doesNotContain("_navn")
    }

    @Test
    fun `landkoder-endepunkt returnerer mappet liste fra kodeverk`() {
        val response =
            restTemplate.exchange<List<Landkode>>(
                localhost("/api/oppslag/landkoder?spraak=nb"),
                org.springframework.http.HttpMethod.GET,
                HttpEntity<String>(headers),
            )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isNotNull
        assertThat(response.body!!.map { it.kode }).contains("NOR")
    }

    @Test
    fun `landkoder-endepunkt feiler med 400 ved ugyldig språk`() {
        val exception =
            assertThrows<HttpClientErrorException> {
                restTemplate.exchange<String>(
                    localhost("/api/oppslag/landkoder?spraak=ugyldig"),
                    org.springframework.http.HttpMethod.GET,
                    HttpEntity<String>(headers),
                )
            }

        assertThat(exception.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun `Skal bare ha en av hver CORS-header etter et kall`() {
        headers.origin = "http://localhost:3000"
        val response =
            restTemplate.exchange<Søkerinfo>(
                localhost("/api/oppslag/sokerinfo"),
                org.springframework.http.HttpMethod.GET,
                HttpEntity<String>(headers),
            )

        assertThat(response.headers["Access-Control-Allow-Origin"]).hasSize(1)
        assertThat(response.headers["Access-Control-Allow-Headers"]).hasSize(1)
        assertThat(response.headers["Access-Control-Allow-Credentials"]).hasSize(1)
        assertThat(response.headers["Access-Control-Allow-Methods"]).hasSize(1)
    }
}
