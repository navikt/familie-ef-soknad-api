package no.nav.familie.ef.søknad.service


interface DokumentService {

    fun hentVedlegg(vedleggsId: String): ByteArray

}
