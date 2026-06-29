package no.nav.familie.ef.søknad.minside

import no.nav.familie.ef.søknad.minside.domain.Variantformat
import no.nav.familie.ef.søknad.minside.dto.JournalpostDto
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/journalpost")
@Validated
class JournalpostController(
    private val journalpostService: JournalpostService,
) {
    @GetMapping
    fun hentJournalposter(): List<JournalpostDto> = journalpostService.hentJournalposterForBruker()

    @GetMapping(
        "/{journalpostId}/dokument-pdf/{dokumentInfoId}/variantformat/{dokumentVariantFormat}",
        produces = [MediaType.APPLICATION_PDF_VALUE],
    )
    fun hentPdfDokument(
        @PathVariable journalpostId: String,
        @PathVariable dokumentInfoId: String,
        @PathVariable dokumentVariantFormat: Variantformat,
    ): ByteArray = journalpostService.hentPdfDokument(journalpostId, dokumentInfoId, dokumentVariantFormat)
}
