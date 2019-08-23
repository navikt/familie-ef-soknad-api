package no.nav.familie.ef.soknad.config.interceptor

import org.slf4j.MDC
import org.springframework.core.annotation.Order
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component

import java.io.IOException

@Component
@Order
class MDCValuesPropagatingClientInterceptor : ClientHttpRequestInterceptor {

    companion object {
        const val NAV_CALL_ID = "Nav-Call-Id"
        const val NAV_CONSUMER_ID = "Nav-Consumer-Id"
    }

    @Throws(IOException::class)
    override fun intercept(request: HttpRequest, body: ByteArray, execution: ClientHttpRequestExecution): ClientHttpResponse {

        propagateIfSet(request, NAV_CALL_ID, NAV_CONSUMER_ID)
        return execution.execute(request, body)
    }

    private fun propagateIfSet(request: HttpRequest, vararg keys: String) {
        keys.forEach { key ->
            val value = MDC.get(key)
            if (value != null) {
                request.headers.add(key, value)
            }
        }
    }
}
