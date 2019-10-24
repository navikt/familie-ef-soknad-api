package no.nav.familie.ef.søknad.util

import com.nimbusds.jwt.JWTClaimsSet
import no.nav.familie.ef.søknad.service.ClamAvVirusScanner
import no.nav.security.oidc.context.OIDCClaims
import no.nav.security.oidc.context.OIDCValidationContext
import no.nav.security.oidc.exceptions.OIDCTokenValidatorException
import org.slf4j.LoggerFactory
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
        logger.info("context", context())
        return context()?.getClaims(ISSUER)
    }

    private fun context(): OIDCValidationContext? {
        return OIDCValidationContext()
//  TODO      return ctxHolder.oidcValidationContext
    }
    private val logger = LoggerFactory.getLogger(ClamAvVirusScanner::class.java)

    companion object {

        const val ISSUER = "selvbetjening"
    }
}
