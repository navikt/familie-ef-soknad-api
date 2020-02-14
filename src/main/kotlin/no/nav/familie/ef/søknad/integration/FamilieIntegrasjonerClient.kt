package no.nav.familie.ef.søknad.integration

import no.nav.familie.ef.søknad.config.FamilieIntegrasjonerConfig
import no.nav.familie.ef.søknad.util.URIUtil
import no.nav.familie.kontrakter.felles.Ressurs
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations

@Component
internal class FamilieIntegrasjonerClient(val config: FamilieIntegrasjonerConfig,
                                          operations: RestOperations) : PingableRestClient(operations, config.pingUri) {

    fun hentPoststedFor(postnummer: String): String? {
        val ressurs = getForEntity(URIUtil.uri(config.poststedUri, postnummer)) as Ressurs<String>
        return ressurs.data
    }
}
