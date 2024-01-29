package no.nav.familie.ef.søknad.minside

import no.nav.familie.ef.søknad.minside.domain.Journalpost
import no.nav.familie.sikkerhet.EksternBrukerUtils
import org.springframework.stereotype.Service

@Service
class DokumentService(private val safClient: SafClient) {
    fun hentJournalposterForBruker(): List<Journalpost> {
        return safClient.hentJournalposterForBruker(EksternBrukerUtils.hentFnrFraToken())
            .dokumentoversiktSelvbetjening.tema.find { tema -> tema.kode == "ENF" }?.journalposter ?: emptyList()
    }
}
