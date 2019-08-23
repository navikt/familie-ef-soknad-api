package no.nav.familie.ef.soknad.api

import no.nav.familie.ef.soknad.api.dto.KvitteringDto
import no.nav.familie.ef.soknad.api.dto.SøknadDto
import no.nav.familie.ef.soknad.service.SøknadService
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/rest/soknad"], produces = [APPLICATION_JSON_VALUE])
class SøknadController(val søknadService: SøknadService) {

    @PostMapping("sendInn")
    fun sendInn(@RequestBody søknadDto: SøknadDto): KvitteringDto {
        return søknadService.sendInn(søknadDto)
    }

}