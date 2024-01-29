package no.nav.familie.ef.søknad.minside

import no.nav.familie.ef.søknad.infrastruktur.OppslagSpringRunnerTest
import no.nav.familie.ef.søknad.minside.dto.JournalpostDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod

internal class DokumentControllerTest : OppslagSpringRunnerTest() {

    val tokenSubject = "12345678911"

    @BeforeEach
    fun setUp() {
        headers.setBearerAuth(søkerBearerToken(tokenSubject))
    }

    @Test
    fun `skal hente dokumenter for privatperson`() {
        val response = restTemplate.exchange<List<JournalpostDto>>(
            localhost("/api/dokument/journalposter"),
            HttpMethod.GET,
            HttpEntity<String>(headers),
        )

        assertThat(response.body.isNotEmpty())
    }
}
