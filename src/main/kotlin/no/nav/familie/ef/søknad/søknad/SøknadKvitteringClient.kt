package no.nav.familie.ef.søknad.søknad

import no.nav.familie.ef.søknad.infrastruktur.config.MottakConfig
import no.nav.familie.http.client.AbstractPingableRestClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.web.client.RestOperations
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Service
class SøknadKvitteringClient(
    private val config: MottakConfig,
    @Qualifier("tokenExchange") operations: RestOperations,
) : AbstractPingableRestClient(
        operations,
        "søknad.henting",
    ) {
    override val pingUri: URI = config.pingUri

    fun hentSøknadKvittering(søknadId: String): ByteArray =
        getForEntity<ByteArray>(
            UriComponentsBuilder
                .fromUriString(
                    "${config.hentSøknadKvitteringUri}/$søknadId",
                ).build()
                .toUri(),
            HttpHeaders().medContentTypeJsonUTF8(),
        )
}

fun HttpHeaders.medContentTypeJsonUTF8(): HttpHeaders {
    this.add("Content-Type", "application/json;charset=UTF-8")
    this.acceptCharset = listOf(Charsets.UTF_8)
    return this
}
