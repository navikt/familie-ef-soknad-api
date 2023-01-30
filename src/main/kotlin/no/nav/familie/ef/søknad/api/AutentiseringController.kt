package no.nav.familie.ef.søknad.api

import no.nav.familie.sikkerhet.EksternBrukerUtils
import no.nav.security.token.support.core.api.ProtectedWithClaims
import no.nav.security.token.support.core.api.RequiredIssuers
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api"], produces = [MediaType.TEXT_PLAIN_VALUE])
@RequiredIssuers(
    ProtectedWithClaims(issuer = EksternBrukerUtils.ISSUER_TOKENX, claimMap = ["acr=Level4"]),
    ProtectedWithClaims(issuer = EksternBrukerUtils.ISSUER_SELVBETJENING, claimMap = ["acr=Level4"]),
)
class AutentiseringController {

    @GetMapping("/innlogget")
    fun verifiserAutentisering(): String {
        return "Autentisert kall"
    }
}
