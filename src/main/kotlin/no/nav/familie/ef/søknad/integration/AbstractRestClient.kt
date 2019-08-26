package no.nav.familie.ef.søknad.integration

import org.slf4j.LoggerFactory
import org.slf4j.MarkerFactory
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestOperations
import java.net.URI

abstract class AbstractRestClient(protected val operations: RestOperations) {

    companion object {
        protected val CONFIDENTIAL = MarkerFactory.getMarker("CONFIDENTIAL")
        protected val LOG = LoggerFactory.getLogger(this::class.java)
    }

    protected abstract val isEnabled: Boolean

    protected inline fun <reified T> getForObject(uri: URI): T? = getForObject(uri, true)

    protected inline fun <reified T> getForObject(uri: URI, throwOnNotFound: Boolean): T? {
        try {
            val respons = operations.getForObject(uri, T::class.java)
            if (respons != null) {
                LOG.trace(CONFIDENTIAL, "Respons: {}", respons)
            }
            return respons
        } catch (e: HttpClientErrorException) {
            if (!throwOnNotFound && NOT_FOUND == e.statusCode) {
                LOG.info("Fant intet objekt på {}, returnerer null", uri)
                return null
            }
            throw e
        }
    }

    protected inline fun <reified T> postForObject(uri: URI, payload: Any): T? =
            operations.postForObject(uri, payload, T::class.java)

    override fun toString(): String = this::class.simpleName + " [operations=" + operations + "]"
}
