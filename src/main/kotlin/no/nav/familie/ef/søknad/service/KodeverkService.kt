package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.integration.FamilieIntegrasjonerClient
import org.springframework.stereotype.Service

@Service
class KodeverkService(private val integrasjonerClient: FamilieIntegrasjonerClient) {

    fun hentPoststedFor(postnummer: String): String? {
        return integrasjonerClient.hentPoststedFor(postnummer)
    }

}