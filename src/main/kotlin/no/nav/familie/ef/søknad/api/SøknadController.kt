package no.nav.familie.ef.søknad.api

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.api.dto.Søknad
import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
import no.nav.familie.ef.søknad.featuretoggle.enabledEllersHttp404
import no.nav.familie.ef.søknad.service.SøknadService
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(path = ["/api/soknad"], produces = [APPLICATION_JSON_VALUE])
class SøknadController(val søknadService: SøknadService, val featureToggleService: FeatureToggleService) {

    @PostMapping("sendInn")
    fun sendInn(@RequestBody søknad: Søknad): Kvittering {

        return featureToggleService.enabledEllersHttp404("familie.ef.soknad.send-soknad") {
            søknadService.sendInn(søknad)
        }
    }
}
