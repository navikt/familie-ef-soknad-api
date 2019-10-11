package no.nav.familie.ef.søknad.api


import no.nav.familie.ef.søknad.api.dto.Søkerinfo
import no.nav.familie.ef.søknad.service.Oppslag
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = [OppslagController.OPPSLAG], produces = [APPLICATION_JSON_VALUE])
//@ProtectedWithClaims(issuer = ISSUER, claimMap = ["acr=Level4"])
class OppslagController(private val oppslag: Oppslag) {

    @GetMapping("/sokerinfo")
    fun søkerinfo(): Søkerinfo {
        return oppslag.hentSøkerinfo()
    }

    companion object {
        const val OPPSLAG = "/api/oppslag"
    }
}
