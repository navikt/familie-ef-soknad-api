package no.nav.familie.ef.søknad.infrastruktur.health

import no.nav.security.token.support.core.api.Unprotected
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Unprotected
@RequestMapping(path = ["/api"], produces = [MediaType.APPLICATION_JSON_VALUE])
class StatusController {

    @GetMapping("/status")
    fun status(): StatusDto {
        return StatusDto(Plattformstatus.OK, "Alt er bra")
    }
}

const val LOG_URL = "https://logs.adeo.no/app/discover#/view/a3e93b80-c1a5-11ee-a029-75a0ed43c092?_g=()"
data class StatusDto(val status: Plattformstatus, val description: String? = null, val logLink: String? = LOG_URL)

// OK, ISSUE, DOWN
enum class Plattformstatus {
    OK
}
