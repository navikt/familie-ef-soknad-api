package no.nav.familie.ef.søknad.integration

import org.springframework.web.client.RestOperations
import java.net.URI

abstract class PingableRestClient(operations: RestOperations, private val pingUri: URI): AbstractRestClient(operations) {

    fun pingURI(): URI {
        return pingUri
    }

    fun ping(): String = getForEntity(pingUri)

}
