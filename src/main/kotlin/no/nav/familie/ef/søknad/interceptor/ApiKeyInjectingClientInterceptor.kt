package no.nav.familie.ef.søknad.interceptor

import org.slf4j.LoggerFactory
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse

import java.io.IOException
import java.net.URI

class ApiKeyInjectingClientInterceptor(private val apiKeys: Map<URI, Pair<String, String>>) : ClientHttpRequestInterceptor {

    private val logger = LoggerFactory.getLogger(ApiKeyInjectingClientInterceptor::class.java)


    @Throws(IOException::class)
    override fun intercept(request: HttpRequest, body: ByteArray, execution: ClientHttpRequestExecution): ClientHttpResponse {
        val apiKey = apiKeyFor(request.uri)
        if (apiKey != null) {
            logger.info("Injisert API-key som header {} for {}", apiKey.first, request.uri)
            request.headers.add(apiKey.first, apiKey.second)
        } else {
            logger.info("Ingen API-key ble funnet for {} (sjekket {} konfigurasjoner)", request.uri,
                         apiKeys.values.size)
        }
        return execution.execute(request, body)
    }

    private fun apiKeyFor(uri: URI): Pair<String, String>? {
        return apiKeys.entries
                .filter { s -> uri.toString().startsWith(s.key.toString()) }
                .map { it.value }
                .firstOrNull()
    }

    override fun toString(): String {
        return javaClass.simpleName + " [apiKeys=" + apiKeys.keys + "]"
    }
}
