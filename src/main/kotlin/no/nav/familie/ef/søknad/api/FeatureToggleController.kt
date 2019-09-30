package no.nav.familie.ef.søknad.api

import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/featuretoggle"], produces = [MediaType.APPLICATION_JSON_VALUE])
class FeatureToggleController (private val featureToggleService: FeatureToggleService) {

    @GetMapping("/{toggleId}")
    fun sjekkFunksjonsbryter(@PathVariable toggleId: String, @RequestParam("defaultverdi") defaultVerdi:Boolean?=false) : Boolean {
        return featureToggleService.isEnabled(toggleId, defaultVerdi ?: false)
    }
}
