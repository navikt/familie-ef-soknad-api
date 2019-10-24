package no.nav.familie.ef.søknad.api

import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
import no.nav.security.oidc.api.Unprotected
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/api/featuretoggle"], produces = [MediaType.APPLICATION_JSON_VALUE])
@Unprotected
class FeatureToggleController (private val featureToggleService: FeatureToggleService) {

    val funksjonsbrytere = listOf("familie.ef.soknad.send-soknad", "familie.ef.soknad.vis-innsending")

    @GetMapping
    fun sjekkAlle() : Map<String, Boolean> {
        val toggleVerdier = funksjonsbrytere.map { toggle -> toggle to featureToggleService.isEnabled(toggle)}.toMap()
        return toggleVerdier
    }

    @GetMapping("/{toggleId}")
    fun sjekkFunksjonsbryter(@PathVariable toggleId: String,
                             @RequestParam("defaultverdi") defaultVerdi:Boolean?=false) : Boolean {
        return featureToggleService.isEnabled(toggleId, defaultVerdi ?: false)
    }
}
