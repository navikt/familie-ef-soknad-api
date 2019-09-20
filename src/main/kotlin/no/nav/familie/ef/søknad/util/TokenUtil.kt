package no.nav.familie.ef.søknad.util

import com.nimbusds.jwt.JWTClaimsSet
import no.nav.security.oidc.context.OIDCClaims
import no.nav.security.oidc.context.OIDCValidationContext
import no.nav.security.oidc.exceptions.OIDCTokenValidatorException
import org.springframework.stereotype.Component
import java.util.*

@Component
class TokenUtil(
//  TODO   private val ctxHolder: OIDCRequestContextHolder
) {

    val expiryDate: Date?
        get() = claimSet()?.expirationTime

    val subject: String?
        get() = claimSet()?.subject

    val fødselsnummer: String
        get() = subject ?: throw OIDCTokenValidatorException("Fant ikke subject", expiryDate)


    private fun claimSet(): JWTClaimsSet? {
        return claims()?.claimSet
    }

    private fun claims(): OIDCClaims? {
        return context()?.getClaims(ISSUER)
    }

    private fun context(): OIDCValidationContext? {
        return OIDCValidationContext()
//  TODO      return ctxHolder.oidcValidationContext
    }

    companion object {

        const val ISSUER = "selvbetjening"
    }
}
