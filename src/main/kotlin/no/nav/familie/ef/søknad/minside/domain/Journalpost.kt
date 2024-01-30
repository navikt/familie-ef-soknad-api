package no.nav.familie.ef.søknad.minside.domain

import no.nav.familie.kontrakter.felles.journalpost.Journalposttype
import no.nav.familie.kontrakter.felles.journalpost.Journalstatus
import no.nav.familie.kontrakter.felles.journalpost.RelevantDato

data class DokumentoversiktSelvbetjeningResponse(val dokumentoversiktSelvbetjening: DokumentoversiktSelvbetjening) {
    fun efJournalposter(): List<Journalpost> =
        this.dokumentoversiktSelvbetjening.tema.find { tema -> tema.kode == "ENF" }?.journalposter ?: emptyList()
}

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
) {
    fun relevanteDokumenter(): List<DokumentInfo> =
        this.dokumenter.filter { dokument -> dokument.mestRelevantVariantFormat()?.brukerHarTilgang ?: false }

    fun harRelevanteDokumenter(): Boolean = this.relevanteDokumenter().isNotEmpty()
}

data class DokumentInfo(
    val dokumentInfoId: String,
    val tittel: String,
    val dokumentvarianter: List<DokumentVariant>,
) {
    fun mestRelevantVariantFormat(): DokumentVariant? {
        return if (dokumentvarianter.any { it.variantformat == Variantformat.SLADDET }) {
            dokumentvarianter.find { it.variantformat == Variantformat.SLADDET }
        } else {
            dokumentvarianter.find { it.variantformat == Variantformat.ARKIV }
        }
    }
}

data class DokumentVariant(
    val variantformat: Variantformat,
    val brukerHarTilgang: Boolean,
    val filtype: String,
)

enum class Variantformat {
    ARKIV,
    SLADDET,
}
