package no.nav.familie.ef.søknad.api

import no.nav.security.oidc.api.Unprotected
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api/"], produces = [MediaType.APPLICATION_JSON_VALUE])
@Unprotected
class PingController() {

    @GetMapping("/ping")
    fun ping(): String {
        return " Ack - vi har kontakt"
    }

}