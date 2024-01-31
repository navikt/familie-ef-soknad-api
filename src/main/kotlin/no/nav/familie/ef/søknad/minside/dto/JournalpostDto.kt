package no.nav.familie.ef.søknad.minside.dto

import no.nav.familie.ef.søknad.minside.domain.DokumentInfo
import no.nav.familie.ef.søknad.minside.domain.Journalpost
import no.nav.familie.ef.søknad.minside.domain.JournalpostDatoUtil.mestRelevanteDato
import no.nav.familie.ef.søknad.minside.domain.Variantformat
import no.nav.familie.kontrakter.felles.journalpost.Journalposttype
import java.time.LocalDateTime

data class JournalpostDto(
    val journalpostId: String,
    val journalpostType: Journalposttype,
    val dato: LocalDateTime?,
    val hovedDokument: DokumentInfoDto,
    val vedlegg: List<DokumentInfoDto>,
)

data class DokumentInfoDto(
    val dokumentId: String,
    val tittel: String,
    val variantformat: Variantformat,
    val filtype: String,
)

fun Journalpost.tilDto() = JournalpostDto(
    journalpostId = this.journalpostId,
    journalpostType = this.journalposttype,
    dato = mestRelevanteDato(this),
    hovedDokument = this.tilHovedDokumentDto(),
    vedlegg = this.tilVedleggDto(),
)

fun Journalpost.tilHovedDokumentDto() = this.relevanteDokumenter().first().tilDto()

fun Journalpost.tilVedleggDto(): List<DokumentInfoDto> {
    val dokumenter = this.relevanteDokumenter()
    return dokumenter.subList(1, dokumenter.count()).map { it.tilDto() }
}

fun Journalpost.erInngåendeEllerUtgåendeJournalpost(): Boolean =
    this.journalposttype == Journalposttype.I || this.journalposttype == Journalposttype.U

fun DokumentInfo.tilDto(): DokumentInfoDto {
    val dokumentvariant = this.mestRelevantDokumentVariant()
        ?: throw IllegalStateException("Dokumentet mangler variantformat - det skal filtereres vekk før den kommer så langt som dette")
    return DokumentInfoDto(
        dokumentId = this.dokumentInfoId,
        tittel = this.tittel,
        variantformat = dokumentvariant.variantformat,
        filtype = dokumentvariant.filtype,
    )
}
