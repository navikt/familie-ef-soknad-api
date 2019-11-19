package no.nav.familie.ef.søknad.api;

import no.nav.familie.ef.søknad.util.InnloggingUtils
import no.nav.security.token.support.core.api.ProtectedWithClaims

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/api"], produces = [MediaType.TEXT_PLAIN_VALUE])
@ProtectedWithClaims(issuer = InnloggingUtils.ISSUER, claimMap = ["acr=Level4"])
class AutentiseringController {

    @GetMapping("/innlogget")
    fun verifiserAutentisering(): String {
        return "Autentisert kall"
    }
}
