package no.nav.familie.ef.søknad.service


interface Dokument {

    fun hentVedlegg(vedleggsId: String): String?

}
