package no.nav.familie.ef.søknad.minside.domain

import no.nav.familie.kontrakter.felles.journalpost.Journalposttype
import no.nav.familie.kontrakter.felles.journalpost.Journalstatus
import no.nav.familie.kontrakter.felles.journalpost.RelevantDato
import java.time.LocalDateTime

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
        this.dokumenter.filter { dokument -> dokument.mestRelevantDokumentVariant()?.brukerHarTilgang ?: false }

    fun harRelevanteDokumenter(): Boolean = this.relevanteDokumenter().isNotEmpty()
    fun mestRelevanteDato(journalpost: Journalpost): LocalDateTime? {
        return journalpost.relevanteDatoer.maxByOrNull { datoTyperSortert(it.datotype) }?.dato
    }
}

data class DokumentInfo(
    val dokumentInfoId: String,
    val tittel: String,
    val dokumentvarianter: List<DokumentVariant>,
) {
    fun mestRelevantDokumentVariant(): DokumentVariant? {
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

private fun datoTyperSortert(datoType: String) = when (datoType) {
    "DATO_REGISTRERT" -> 4
    "DATO_JOURNALFOERT" -> 3
    "DATO_DOKUMENT" -> 2
    "DATO_OPPRETTET" -> 1
    else -> 0
}
