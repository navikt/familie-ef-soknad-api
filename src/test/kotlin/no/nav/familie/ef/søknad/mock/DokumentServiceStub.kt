package no.nav.familie.ef.søknad.mock

import no.nav.familie.ef.søknad.service.DokumentService

class DokumentServiceStub : DokumentService {

    override fun hentVedlegg(vedleggsId: String): ByteArray {
        return "vedleggData".toByteArray()
    }

}
