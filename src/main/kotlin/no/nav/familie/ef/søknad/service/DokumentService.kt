package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Dokumentasjonsbehov


interface DokumentService {

    fun hentVedlegg(vedleggsId: String): ByteArray

    fun hentDokumenter(dokumentasjonsbehov: List<Dokumentasjonsbehov>): Map<String, ByteArray> =
            dokumentasjonsbehov.flatMap { dok ->
                dok.opplastedeVedlegg.map { it.dokumentId to hentVedlegg(it.dokumentId) }
            }.toMap()
}
