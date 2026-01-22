package no.nav.familie.ef.søknad.minside

import no.nav.familie.ef.søknad.infrastruktur.OppslagSpringRunnerTest
import no.nav.familie.ef.søknad.minside.domain.Variantformat
import no.nav.familie.ef.søknad.minside.dto.JournalpostDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.client.exchange
import java.time.LocalDate

internal class JournalpostControllerTest : OppslagSpringRunnerTest() {
    val tokenSubject = "12345678911"

    @BeforeEach
    fun setUp() {
        headers.setBearerAuth(søkerBearerToken(tokenSubject))
    }

    @Test
    fun `skal hente journalposter for privatperson`() {
        val response =
            restTemplate.exchange<List<JournalpostDto>>(
                localhost("/api/journalpost"),
                HttpMethod.GET,
                HttpEntity<String>(headers),
            )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.isNotEmpty())

        // Totalt 6 journalposter - én blankett (N) og én som går til advokat (ikke tilgang)
        // Siste skal filtreres vekk

        assertThat(response.body?.size).isEqualTo(5)

        val journalpostMedSladdetVariantformat = response.body?.find { it.journalpostId == "453853070" }
        assertThat(journalpostMedSladdetVariantformat?.hovedDokument?.variantformat).isEqualTo(Variantformat.SLADDET)

        val journalpostMedForskjelligeDatoer = response.body?.first { it.journalpostId == "453858394" }
        assertThat(journalpostMedForskjelligeDatoer?.dato?.toLocalDate()).isEqualTo(LocalDate.of(2024, 1, 24))

        val journalpostMedFlereDokumenter = response.body?.first { it.journalpostId == "453858394" }
        assertThat(journalpostMedFlereDokumenter?.hovedDokument?.dokumentId).isEqualTo("454251341")
        assertThat(journalpostMedFlereDokumenter?.hovedDokument?.tittel).isEqualTo("Ettersending overgangsstønad - enslig mor eller far")
        assertThat(journalpostMedFlereDokumenter?.vedlegg).hasSize(1)
        assertThat(journalpostMedFlereDokumenter?.vedlegg?.first()?.dokumentId).isEqualTo("454251342")
    }

    @Test
    fun `skal hente dokument for privatperson`() {
        val response =
            restTemplate.exchange<ByteArray>(
                localhost("/api/journalpost/1234/dokument-pdf/8/variantformat/ARKIV"),
                HttpMethod.GET,
                HttpEntity<String>(headers),
            )

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isNotNull
    }
}
