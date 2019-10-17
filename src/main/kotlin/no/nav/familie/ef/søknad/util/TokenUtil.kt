package no.nav.familie.ef.søknad.util

import no.nav.security.token.support.core.context.TokenValidationContext
import no.nav.security.token.support.core.exceptions.JwtTokenValidatorException
import no.nav.security.token.support.core.jwt.JwtTokenClaims
import no.nav.security.token.support.spring.SpringTokenValidationContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder

@Component
class TokenUtil() {

    val subject: String?
        get() = claims()?.subject

    val fødselsnummer: String
        get() = subject ?: throw JwtTokenValidatorException("Fant ikke subject")

    private fun claims(): JwtTokenClaims? {
        val attribute = RequestContextHolder.currentRequestAttributes().getAttribute(getContextHolderName(), 0) as TokenValidationContext
        return attribute.getClaims(ISSUER)
    }

    private fun getContextHolderName(): String {
        val holder = "no.nav.security.token.support.spring.SpringTokenValidationContextHolder"
        return SpringTokenValidationContextHolder::class.qualifiedName ?: holder
    }

    companion object {
        const val ISSUER = "selvbetjening"
    }
}


