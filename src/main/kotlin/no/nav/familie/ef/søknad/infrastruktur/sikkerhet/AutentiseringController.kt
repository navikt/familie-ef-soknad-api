package no.nav.familie.ef.søknad.infrastruktur.sikkerhet

import no.nav.familie.sikkerhet.EksternBrukerUtils
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api"])
@ProtectedWithClaims(issuer = EksternBrukerUtils.ISSUER_TOKENX, claimMap = ["acr=Level4"])
class AutentiseringController {
    @GetMapping("/innlogget")
    fun verifiserAutentisering(): AuthResponse = AuthResponse("Autentisert kall")
}

data class AuthResponse(
    val message: String,
)
