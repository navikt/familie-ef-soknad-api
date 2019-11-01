package no.nav.familie.ef.søknad.integration

import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestOperations
import org.springframework.web.client.getForEntity
import java.net.URI

abstract class PingableRestClient(operations: RestOperations, val pingUri: URI) : AbstractRestClient(operations) {

    fun pingURI(): URI {
        return pingUri
    }

    open fun ping() {
        val respons: ResponseEntity<String> = operations.getForEntity(pingUri)
        if (!respons.statusCode.is2xxSuccessful) {
            throw HttpServerErrorException(respons.statusCode)
        }
    }
}

