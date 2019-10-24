package no.nav.familie.ef.søknad.util

import no.nav.security.oidc.OIDCConstants
import no.nav.security.oidc.context.OIDCValidationContext
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder

object InnloggingUtils {

    private const val selvbetjening = "selvbetjening"

    private val context
        get() =
            RequestContextHolder
                    .currentRequestAttributes()
                    .getAttribute(OIDCConstants.OIDC_VALIDATION_CONTEXT,
                                  RequestAttributes.SCOPE_REQUEST) as OIDCValidationContext? ?: OIDCValidationContext()

    fun hentFnrFraToken(): String {
        return context.getClaims(selvbetjening).claimSet.subject
    }

    fun generateBearerTokenForLoggedInUser(): String {
        return if (context.hasValidToken())
            context.issuers.joinToString("Bearer ") { context.getToken(it).idToken }
        else ""
    }

}
