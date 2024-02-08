package no.nav.familie.ef.søknad.minside

import no.nav.familie.ef.søknad.infrastruktur.OppslagSpringRunnerTest
import no.nav.familie.ef.søknad.minside.dto.MineStønaderDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

internal class SaksbehandlingControllerTest : OppslagSpringRunnerTest() {

    val tokenSubject = "12345678911"

    @BeforeEach
    fun setUp() {
        headers.setBearerAuth(søkerBearerToken(tokenSubject))
    }

    @Test
    fun `skal hente utbetalingsperioder for privatperson`() {
        val response = restTemplate.exchange<MineStønaderDto>(
            localhost("/api/saksbehandling/stønadsperioder"),
            HttpMethod.GET,
            HttpEntity<String>(headers),
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body.overgangsstønad.isNotEmpty())
        assertThat(response.body.barnetilsyn.isNotEmpty())
        assertThat(response.body.skolepenger.isNotEmpty())
    }
}
