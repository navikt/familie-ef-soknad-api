package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.integration.FamilieDokumentClient
import org.springframework.stereotype.Service

@Service
internal class DokumentService(private val client: FamilieDokumentClient) : Dokument {

    override fun hentVedlegg(vedleggsId: String): String? {
        return client.hentVedlegg(vedleggsId)
    }
}
