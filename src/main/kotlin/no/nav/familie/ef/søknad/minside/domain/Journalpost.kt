package no.nav.familie.ef.søknad.minside.domain

import no.nav.familie.kontrakter.felles.journalpost.Journalposttype
import no.nav.familie.kontrakter.felles.journalpost.Journalstatus
import no.nav.familie.kontrakter.felles.journalpost.RelevantDato

data class DokumentoversiktSelvbetjeningResponse(val dokumentoversiktSelvbetjening: DokumentoversiktSelvbetjening)
data class DokumentoversiktSelvbetjening(val tema: List<Tema>)

data class Tema(
    val navn: String,
    val kode: String,
    val journalposter: List<Journalpost>,
)

data class Journalpost(
    val journalpostId: String,
    val tittel: String,
    val journalposttype: Journalposttype,
    val journalstatus: Journalstatus,
    val relevanteDatoer: List<RelevantDato>,
    val dokumenter: List<DokumentInfo>,
)

data class DokumentInfo(
    val tittel: String,
    val dokumentInfoId: String,
    val dokumentvarianter: List<DokumentVariant>,
)

data class DokumentVariant(
    val variantformat: Variantformat,
    val brukerHarTilgang: Boolean,
    val filtype: String,
)

enum class Variantformat {
    ARKIV,
    SLADDET,
}
