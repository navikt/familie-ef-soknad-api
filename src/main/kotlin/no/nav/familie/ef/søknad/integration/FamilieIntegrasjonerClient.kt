package no.nav.familie.ef.søknad.integration

import no.nav.familie.ef.søknad.config.FamilieIntegrasjonerConfig
import no.nav.familie.http.client.AbstractPingableRestClient
import no.nav.familie.kontrakter.felles.Ressurs
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Component
class FamilieIntegrasjonerClient(private val config: FamilieIntegrasjonerConfig,
                                 operations: RestOperations) : AbstractPingableRestClient(operations,
                                                                                          "familie.integrasjoner") {

    override val pingUri: URI = config.pingUri

    internal fun poststedUri(postnummer: String) =
            UriComponentsBuilder.fromUri(config.poststedUri).path(postnummer).build().toUri()

    fun hentPoststedFor(postnummer: String): String? {
        val ressurs: Ressurs<String> = getForEntity(poststedUri(postnummer))
        return ressurs.data
    }
}
