package no.nav.familie.ef.søknad.api

import no.nav.security.token.support.core.api.Protected
import no.nav.security.token.support.core.api.ProtectedWithClaims
import no.nav.security.token.support.core.api.Unprotected
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api"], produces = [MediaType.TEXT_PLAIN_VALUE])
@ProtectedWithClaims(issuer = "idporten", claimMap = ["acr=Level4"])
class AutentiseringController {

    @GetMapping("/innlogget")
    fun verifiserAutentisering(): String {
        return "Autentisert kall"
    }
}
