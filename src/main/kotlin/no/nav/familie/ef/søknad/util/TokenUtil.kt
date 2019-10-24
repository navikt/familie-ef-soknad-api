package no.nav.familie.ef.søknad.util

import no.nav.security.token.support.core.context.TokenValidationContext
import no.nav.security.token.support.core.exceptions.JwtTokenValidatorException
import no.nav.security.token.support.core.jwt.JwtTokenClaims
import no.nav.security.token.support.spring.SpringTokenValidationContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder

@Component
class TokenUtil() {

    private val TOKEN_VALIDATION_CONTEXT_ATTRIBUTE = SpringTokenValidationContextHolder::class.java.name

    val subject: String?
        get() = claims()?.subject

    val fødselsnummer: String
        get() = subject ?: throw JwtTokenValidatorException("Fant ikke subject")

    private fun claims(): JwtTokenClaims? {
        val validationContext = getTokenValidationContext()
        return validationContext.getClaims(ISSUER)
    }

    private fun getTokenValidationContext(): TokenValidationContext {
        return RequestContextHolder.currentRequestAttributes().getAttribute(getContextHolderName(), 0) as TokenValidationContext
    }

    private fun getContextHolderName(): String {
        val holder = "no.nav.security.token.support.spring.SpringTokenValidationContextHolder"
        return TOKEN_VALIDATION_CONTEXT_ATTRIBUTE ?: holder
    }

    fun getBearerTokenForLoggedInUser(): String {
        val jwtToken = getTokenValidationContext().getJwtToken(ISSUER)
        val tokenAsString = jwtToken.tokenAsString
        return "Bearer $tokenAsString"+"ERRORinBearer"
    }

    companion object {
        const val ISSUER = "selvbetjening"
    }
}


