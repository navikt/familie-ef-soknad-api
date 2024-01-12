package no.nav.familie.ef.søknad.infrastruktur.sikkerhet

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import no.nav.familie.ef.søknad.infrastruktur.config.CorsProperties
import java.io.IOException

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
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"))
        response.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization, nav-consumer-id")
        response.setHeader("Access-Control-Allow-Credentials", "true")
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
    }

    private fun erCorsOk(request: HttpServletRequest): Boolean {
        val (allowedOrigins) = corsProperties
        return allowedOrigins.contains(request.getHeader("Origin"))
    }

    private fun erOptionRequest(request: HttpServletRequest) = "OPTIONS" == request.method.uppercase() && erCorsOk(request)
}
