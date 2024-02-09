package no.nav.familie.ef.søknad.minside

import no.nav.familie.ef.søknad.minside.domain.Journalpost
import no.nav.familie.ef.søknad.minside.domain.Variantformat
import no.nav.familie.ef.søknad.minside.dto.JournalpostDto
import no.nav.familie.ef.søknad.minside.dto.erInngåendeEllerUtgåendeJournalpost
import no.nav.familie.ef.søknad.minside.dto.tilDto
import no.nav.familie.sikkerhet.EksternBrukerUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class JournalpostService(private val safClient: SafClient) {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val secureLogger = LoggerFactory.getLogger("secureLogger")

    fun hentJournalposterForBruker(): List<JournalpostDto> =
        safClient.hentJournalposterForBruker(EksternBrukerUtils.hentFnrFraToken())
            .efJournalposter()
            .filter { it.harRelevanteDokumenter()}
            .map { it.tilDto() }

    fun hentPdfDokument(
        journalpostId: String,
        dokumentInfoId: String,
        dokumentVariantformat: Variantformat,
    ): ByteArray = safClient.hentDokument(journalpostId, dokumentInfoId, dokumentVariantformat)
}
