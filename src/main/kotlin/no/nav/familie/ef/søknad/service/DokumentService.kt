package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Dokumentasjonsbehov
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.DokumentFelt


interface DokumentService {

    fun hentVedlegg(vedleggsId: String): ByteArray

    fun hentDokumenter(dokumentasjonsbehov: List<Dokumentasjonsbehov>): Map<String, ByteArray> =
        dokumentasjonsbehov.flatMap { dok ->
            dok.opplastedeVedlegg.map { it.dokumentId to hentVedlegg(it.dokumentId) }
        }.toMap()

    fun hentDokumenterFraDokumentFelt(vedleggListe: List<DokumentFelt>): Map<String, ByteArray> =
        vedleggListe.associate { it.dokumentId to hentVedlegg(it.dokumentId) }
}
