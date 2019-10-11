package no.nav.familie.ef.søknad.api

import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/api/featuretoggle"], produces = [MediaType.APPLICATION_JSON_VALUE])
class FeatureToggleController(private val featureToggleService: FeatureToggleService) {

    val funksjonsbrytere = listOf("familie.ef.soknad.send-soknad")

    @GetMapping
    fun sjekkAlle() : Map<String, Boolean> {
        val toggleVerdier = funksjonsbrytere.map { toggle -> toggle to featureToggleService.isEnabled(toggle)}.toMap()
        return toggleVerdier
    }

    @GetMapping("/{toggleId}")
    fun sjekkFunksjonsbryter(@PathVariable toggleId: String,
                             @RequestParam("defaultverdi") defaultVerdi: Boolean? = false): Boolean {
        return featureToggleService.isEnabled(toggleId, defaultVerdi ?: false)
    }
}
