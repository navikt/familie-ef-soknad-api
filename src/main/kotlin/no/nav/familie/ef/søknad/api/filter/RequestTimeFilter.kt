package no.nav.familie.ef.søknad.api.filter

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.util.StopWatch
import java.io.IOException

import javax.servlet.*
import javax.servlet.http.*



class RequestTimeFilter : Filter {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {

        val response = servletResponse as HttpServletResponse
        val request = servletRequest as HttpServletRequest
        val timer = StopWatch()
        try {
            timer.start()
        }
        finally {
            timer.stop()
            log(request, response.status, timer)
        }
        filterChain.doFilter(servletRequest, servletResponse)
    }


    companion object {
        private val LOG = LoggerFactory.getLogger(RequestTimeFilter::class.java)

        private fun log(request: HttpServletRequest, code: Int, timer: StopWatch) {
            if (hasError(code)) {
                LOG.warn("{} - {} - ({}). Dette tok {}ms", request.method, request.requestURI, code, timer.totalTimeMillis)
            } else {
                LOG.info("{} - {} - ({}). Dette tok {}ms", request.method, request.requestURI, code, timer.totalTimeMillis)
            }
        }

        private fun hasError(code: Int): Boolean {
            val series = HttpStatus.Series.resolve(code)
            return series == HttpStatus.Series.CLIENT_ERROR || series == HttpStatus.Series.SERVER_ERROR
        }
    }
}
