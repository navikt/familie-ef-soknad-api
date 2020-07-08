package no.nav.familie.ef.søknad.service


interface DokumentService {

    fun hentVedlegg(vedleggsId: String): ByteArray

    // fun hentDokumenter(dokumentasjonsbehov: List<Dokumentasjonsbehov>): Map<String, ByteArray>
}
