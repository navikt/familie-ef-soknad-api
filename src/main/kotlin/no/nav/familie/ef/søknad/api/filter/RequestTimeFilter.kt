package no.nav.familie.ef.søknad.api.filter

import org.slf4j.LoggerFactory
import org.springframework.http.HttpRequest
import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpResponse
import org.springframework.util.StopWatch

import javax.servlet.*
import java.io.IOException

class RequestTimeFilter : Filter {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {

        val response = servletResponse as ClientHttpResponse
        val request = servletRequest as HttpRequest
        val timer = StopWatch()
        try {
            timer.start()
        }
        finally {
            timer.stop()
            log(request, response.statusCode, timer)
        }
        filterChain.doFilter(servletRequest, servletResponse)
    }


    companion object {
        private val LOG = LoggerFactory.getLogger(RequestTimeFilter::class.java)

        private fun log(request: HttpRequest, code: HttpStatus, timer: StopWatch) {
            if (hasError(code)) {
                LOG.warn("{} - {} - ({}). Dette tok {}ms", request.methodValue, request.uri.path, code, timer.totalTimeMillis)
            } else {
                LOG.info("{} - {} - ({}). Dette tok {}ms", request.methodValue, request.uri.path, code, timer.totalTimeMillis)
            }
        }

        private fun hasError(code: HttpStatus): Boolean {
            return code.series() == HttpStatus.Series.CLIENT_ERROR || code.series() == HttpStatus.Series.SERVER_ERROR
        }
    }
}
