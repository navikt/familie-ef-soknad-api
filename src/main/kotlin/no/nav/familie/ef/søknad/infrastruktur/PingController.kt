package no.nav.familie.ef.søknad.infrastruktur

import no.nav.security.token.support.core.api.Unprotected
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Unprotected
@RequestMapping(path = ["/api"], produces = [MediaType.APPLICATION_JSON_VALUE])
class PingController {

    @GetMapping("/ping")
    fun ping(): PingDto {
        return PingDto("Ack - vi har kontakt")
    }
}

data class PingDto(val message: String)
