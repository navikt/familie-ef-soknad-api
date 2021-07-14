package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Dokumentasjonsbehov
import no.nav.familie.kontrakter.ef.søknad.Dokument


interface DokumentService {

    fun hentVedlegg(vedleggsId: String): ByteArray

    fun hentDokumenter(dokumentasjonsbehov: List<Dokumentasjonsbehov>): Map<String, ByteArray> =
            dokumentasjonsbehov.flatMap { dok ->
                dok.opplastedeVedlegg.map { it.dokumentId to hentVedlegg(it.dokumentId) }
            }.toMap()

    fun hentDokumenterÅpenEttersending(dokumenter: List<Dokument>): Map<String, ByteArray> =
        dokumenter.map { it.id to hentVedlegg(it.id) }.toMap()


}
