package no.nav.familie.ef.søknad.minside.dto

import no.nav.familie.ef.søknad.minside.domain.DokumentInfo
import no.nav.familie.ef.søknad.minside.domain.Journalpost
import no.nav.familie.ef.søknad.minside.domain.JournalpostDatoUtil.mestRelevanteDato
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
)

fun Journalpost.tilDto() = JournalpostDto(
    journalpostId = this.journalpostId,
    journalpostType = this.journalposttype,
    dato = mestRelevanteDato(this),
    hovedDokument = this.dokumenter.first().tilDto(), // TODO: Var det slik at første dokument er hoveddokument?
    vedlegg = this.dokumenter.map { it.tilDto() },
)

fun DokumentInfo.tilDto() = DokumentInfoDto(
    dokumentId = this.dokumentInfoId,
    tittel = this.tittel,
)
