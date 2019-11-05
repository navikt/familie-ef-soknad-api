package no.nav.familie.ef.søknad.api.filter

import no.nav.familie.ef.søknad.config.CorsProperties
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@Order(0)
internal class CORSResponseFilter(val corsProperties: CorsProperties) : Filter {

    private val logger = LoggerFactory.getLogger(CORSResponseFilter::class.java)

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
        val request = servletRequest as HttpServletRequest
        val response = servletResponse as HttpServletResponse
        val origin = request.getHeader("Origin") // Referer
        val referer = request.getHeader("Referer") // Referer
        val method = request.method.toUpperCase()

        val (allowedOrigins) = corsProperties
        val corsErOK = allowedOrigins.any { it == referer || it == origin }

        if (corsErOK) {
            logger.warn("($method) Akseptert kall fra origin $origin, referer: $referer")
            response.addHeader("Access-Control-Allow-Origin", "*")
            response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
            response.addHeader("Access-Control-Allow-Credentials", "true")
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
        }

        if ("OPTIONS" == method && corsErOK) {
            logger.warn("Aksepterer OPTIONS $origin, Referer: $referer")
            response.status = HttpServletResponse.SC_OK
        } else {
            filterChain.doFilter(servletRequest, servletResponse)
        }

    }
}
