package no.nav.familie.ef.søknad.api

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.api.dto.Søknad
import no.nav.familie.ef.søknad.service.SøknadService
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/api/soknad"], produces = [APPLICATION_JSON_VALUE])
class SøknadController(val søknadService: SøknadService) {

    @PostMapping("sendInn")
    fun sendInn(@RequestBody søknad: Søknad): Kvittering {
        return søknadService.sendInn(søknad)
    }
}
