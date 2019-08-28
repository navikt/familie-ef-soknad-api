package no.nav.familie.ef.søknad.api

import no.nav.familie.ef.søknad.api.dto.KvitteringDto
import no.nav.familie.ef.søknad.api.dto.SøknadDto
import no.nav.familie.ef.søknad.service.SøknadService
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api/soknad"], produces = [APPLICATION_JSON_VALUE])
class SøknadController(val søknadService: SøknadService) {

    @PostMapping("sendInn")
    fun sendInn(@RequestBody søknadDto: SøknadDto): KvitteringDto {
        return søknadService.sendInn(søknadDto)
    }

}