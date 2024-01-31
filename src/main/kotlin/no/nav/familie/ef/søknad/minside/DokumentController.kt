package no.nav.familie.ef.søknad.minside

import no.nav.familie.ef.søknad.minside.domain.Variantformat
import no.nav.familie.ef.søknad.minside.dto.JournalpostDto
import no.nav.familie.sikkerhet.EksternBrukerUtils
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/dokument")
@ProtectedWithClaims(issuer = EksternBrukerUtils.ISSUER_TOKENX, claimMap = ["acr=Level4"])
@Validated
class DokumentController(
    private val dokumentService: DokumentService,
) {

    @GetMapping("/journalposter")
    fun hentJournalposter(): List<JournalpostDto> {
        return dokumentService.hentJournalposterForBruker()
    }

    @GetMapping(
        "/journalpost/{journalpostId}/dokument-pdf/{dokumentInfoId}/variantformat/{dokumentVariantFormat}",
        produces = [MediaType.APPLICATION_PDF_VALUE],
    )
    fun hentPdfDokument(
        @PathVariable journalpostId: String,
        @PathVariable dokumentInfoId: String,
        @PathVariable dokumentVariantFormat: Variantformat,
    ): ByteArray {
        return dokumentService.hentPdfDokument(journalpostId, dokumentInfoId, dokumentVariantFormat)
    }
}
