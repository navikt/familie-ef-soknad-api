package no.nav.familie.ef.søknad.util

import com.nimbusds.jwt.JWTClaimsSet
import no.nav.security.oidc.OIDCConstants
import no.nav.security.oidc.context.OIDCClaims
import no.nav.security.oidc.context.OIDCValidationContext
import no.nav.security.oidc.exceptions.OIDCTokenValidatorException
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
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
        return context().getClaims(ISSUER)
    }

    private fun context() =
            RequestContextHolder.currentRequestAttributes().getAttribute(OIDCConstants.OIDC_VALIDATION_CONTEXT,
                                                                         RequestAttributes.SCOPE_REQUEST) as OIDCValidationContext

    companion object {
        const val ISSUER = "selvbetjening"
    }
}
