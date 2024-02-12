package no.nav.familie.ef.søknad.minside

import no.nav.familie.ef.søknad.infrastruktur.OppslagSpringRunnerTest
import no.nav.familie.ef.søknad.minside.dto.MineStønaderDto
import no.nav.familie.ef.søknad.minside.dto.PeriodeStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import java.time.LocalDate

internal class SaksbehandlingControllerTest : OppslagSpringRunnerTest() {

    private val tokenSubject = "12345678911"

    // Ønsker ikke å bruke LocalDate.now() i tester -> tar utgangspunkt i 12. feb 2024 som dagens dato
    private val dagensDato = LocalDate.of(2024, 2, 12)

    @BeforeEach
    fun setUp() {
        headers.setBearerAuth(søkerBearerToken(tokenSubject))
    }

    @Test
    fun `skal hente korrekt mappede utbetalingsperioder for bruker`() {
        val response = restTemplate.exchange<MineStønaderDto>(
            localhost("/api/saksbehandling/stonadsperioder"),
            HttpMethod.GET,
            HttpEntity(dagensDato, headers),
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)

        assertThat(response.body.overgangsstønad.periodeStatus).isEqualTo(PeriodeStatus.LØPENDE_UTEN_OPPHOLD)
        assertThat(response.body.overgangsstønad.startDato).isNull()
        assertThat(response.body.overgangsstønad.sluttDato).isEqualTo(LocalDate.of(2025, 9, 30))
        assertThat(response.body.overgangsstønad.perioder.size).isEqualTo(3)

        assertThat(response.body.barnetilsyn.periodeStatus).isEqualTo(PeriodeStatus.TIDLIGERE_ELLER_OPPHOLD)
        assertThat(response.body.barnetilsyn.startDato).isNull()
        assertThat(response.body.barnetilsyn.sluttDato).isNull()
        assertThat(response.body.barnetilsyn.perioder.size).isEqualTo(3)

        assertThat(response.body.skolepenger.periodeStatus).isEqualTo(PeriodeStatus.FREMTIDIG_UTEN_OPPHOLD)
        assertThat(response.body.skolepenger.startDato).isEqualTo(LocalDate.of(2024, 3, 1))
        assertThat(response.body.skolepenger.sluttDato).isEqualTo(LocalDate.of(2027, 1, 31))
        assertThat(response.body.skolepenger.perioder.size).isEqualTo(1)
    }
}
