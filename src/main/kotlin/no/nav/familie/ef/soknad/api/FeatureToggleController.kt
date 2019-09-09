package no.nav.familie.ef.soknad.api

import no.nav.familie.ef.soknad.featuretoggle.UnleashService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.inject.Inject

@RestController
@RequestMapping(path = ["/featuretoggle"], produces = [MediaType.APPLICATION_JSON_VALUE])
class FeatureToggleController @Inject constructor(private val unleashService: UnleashService) {

    @GetMapping("/{toggleId}")
    fun sjekkFunksjonsbryter(@PathVariable toggleId: String, @RequestParam("defaultverdi") defaultVerdi:Boolean?) : Boolean {
        return unleashService.isEnabled(toggleId, defaultVerdi ?: false)
    }
}
