package no.nav.familie.ef.søknad.infrastruktur.sikkerhet

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api"])
class AutentiseringController {
    @GetMapping("/innlogget")
    fun verifiserAutentisering(): AuthResponse = AuthResponse("Autentisert kall")
}

data class AuthResponse(
    val message: String,
)
