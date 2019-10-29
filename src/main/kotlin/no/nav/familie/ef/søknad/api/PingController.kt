package no.nav.familie.ef.søknad.api

import no.nav.familie.ef.søknad.api.dto.Søknad
import no.nav.familie.ef.søknad.util.InnloggingUtils
import no.nav.security.token.support.core.api.ProtectedWithClaims

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/api/"], produces = [MediaType.APPLICATION_JSON_VALUE])
@ProtectedWithClaims(issuer = InnloggingUtils.ISSUER, claimMap = ["acr=Level4"])
class PingController {

    @GetMapping("/ping")
    fun ping(): PingDto {
        return PingDto("Ack - vi har kontakt")
    }

    @PostMapping("/ping", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun pingPost(@RequestBody request: Søknad): PingDto {
        return PingDto(" Ack - vi har mottatt: ${request.text} ${InnloggingUtils.fødselsnummer}")
    }

    @GetMapping("/getToken")
    fun getToken(): PingDto {
        return PingDto(InnloggingUtils.fødselsnummer)
    }

}

data class PingDto(val message: String)
