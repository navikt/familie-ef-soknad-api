package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.integration.FamilieDokumentClient
import org.springframework.stereotype.Service

@Service
internal class DokumentServiceImpl(private val client: FamilieDokumentClient) : DokumentService {

    override fun hentVedlegg(vedleggsId: String): ByteArray {
        return client.hentVedlegg(vedleggsId)
    }
}
