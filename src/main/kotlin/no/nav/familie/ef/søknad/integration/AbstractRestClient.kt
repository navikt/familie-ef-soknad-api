package no.nav.familie.ef.søknad.integration

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.Marker
import org.slf4j.MarkerFactory
import org.springframework.http.*
import org.springframework.web.client.*
import java.net.URI

abstract class AbstractRestClient(protected val operations: RestOperations) {

    private val confidential: Marker = MarkerFactory.getMarker("CONFIDENTIAL")
    protected val log: Logger = LoggerFactory.getLogger(this::class.java)

    protected inline fun <reified T : Any> getForEntity(uri: URI): T {
        val respons: ResponseEntity<T> = operations.getForEntity(uri)
        return validerOgPakkUt(respons, uri)
    }

    protected inline fun <reified T : Any> getForEntity(uri: URI, httpHeaders: HttpHeaders): T {
        val respons: ResponseEntity<T> = operations.exchange(uri, HttpMethod.GET, HttpEntity(null, httpHeaders))
        return validerOgPakkUt(respons, uri)
    }

    protected inline fun <reified T : Any> postForEntity(uri: URI, payload: Any): T {
        val respons: ResponseEntity<T> = operations.postForEntity(uri, payload)
        return validerOgPakkUt(respons, uri)
    }

    protected inline fun <reified T : Any> putForObject(uri: URI, payload: Any): T {
        val respons: ResponseEntity<T> = operations.exchange(RequestEntity.put(uri).body(payload))
        return validerOgPakkUt(respons, uri)
    }

    protected fun <T> validerOgPakkUt(respons: ResponseEntity<T>, uri: URI): T {
        log.trace(confidential, "Respons: {}", respons)
        if (!respons.statusCode.is2xxSuccessful) {
            log.info("Kall mot $uri feilet: ${respons.body}")
            throw HttpServerErrorException(respons.statusCode)
        }
        return respons.body ?: throw IllegalStateException()
    }

    override fun toString(): String = this::class.simpleName + " [operations=" + operations + "]"
}
