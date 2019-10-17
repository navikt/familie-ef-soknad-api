package no.nav.familie.ef.søknad.api

import no.nav.familie.ef.søknad.api.dto.Søknad
import no.nav.familie.ef.søknad.util.TokenUtil
import no.nav.security.token.support.core.api.Protected

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/api/"], produces = [MediaType.APPLICATION_JSON_VALUE])
@Protected
class PingController {

    @GetMapping("/ping")
    fun ping(): String {
        return "Ack - vi har kontakt"
    }

    @PostMapping("/ping", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun pingPost(@RequestBody request: Søknad): String {
        return " Ack - vi har mottatt: ${request.text} ${TokenUtil().fødselsnummer}"
    }


    @GetMapping("/getToken")
    fun getToken(): String {
        return TokenUtil().fødselsnummer
    }

}