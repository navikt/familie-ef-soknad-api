package no.nav.familie.ef.søknad.api.filter

import no.nav.familie.ef.søknad.config.CorsProperties
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@Order(1)
internal class CORSResponseFilter(val corsProperties: CorsProperties) : Filter {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
        val request = servletRequest as HttpServletRequest
        val response = servletResponse as HttpServletResponse
        if (erCorsOk(request)) {
            setCorsHeaders(response, request)
        }

        if (erOptionRequest(request)) {
            response.status = HttpServletResponse.SC_OK
        } else {
            filterChain.doFilter(servletRequest, servletResponse)
        }
    }

    private fun setCorsHeaders(response: HttpServletResponse, request: HttpServletRequest) {
        response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"))
        response.addHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
        response.addHeader("Access-Control-Allow-Credentials", "true")
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
    }

    private fun erCorsOk(request: HttpServletRequest): Boolean {
        val (allowedOrigins) = corsProperties
        return allowedOrigins.contains(request.getHeader("Origin"))
    }

    private fun erOptionRequest(request: HttpServletRequest) = "OPTIONS" == request.method.toUpperCase() && erCorsOk(request)
}
