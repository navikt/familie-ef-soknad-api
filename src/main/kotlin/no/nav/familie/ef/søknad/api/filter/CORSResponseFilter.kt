package no.nav.familie.ef.søknad.api.filter

import no.nav.familie.ef.søknad.config.CorsProperties
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@Order(0)
internal class CORSResponseFilter(val corsProperties: CorsProperties) : Filter {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
        val request = servletRequest as HttpServletRequest
        val response = servletResponse as HttpServletResponse
        val corsErOK = erCorsOk(request)
        if (corsErOK) {
            response.addHeader("Access-Control-Allow-Origin", "*")
            response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
            response.addHeader("Access-Control-Allow-Credentials", "true")
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
        }
        if (erOptionRequest(request) && corsErOK) {
            response.status = HttpServletResponse.SC_OK
        } else {
            filterChain.doFilter(servletRequest, servletResponse)
        }
    }

    private fun erCorsOk(request: HttpServletRequest): Boolean {
        val (allowedOrigins) = corsProperties
        return allowedOrigins.contains(request.getHeader("Origin"))
    }

    private fun erOptionRequest(request: HttpServletRequest) = "OPTIONS" == request.method.toUpperCase()
}
