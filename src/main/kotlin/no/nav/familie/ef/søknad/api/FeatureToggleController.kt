package no.nav.familie.ef.søknad.api

import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
import no.nav.security.token.support.core.api.Unprotected
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/api/featuretoggle"], produces = [MediaType.APPLICATION_JSON_VALUE])
@Unprotected
class FeatureToggleController(private val featureToggleService: FeatureToggleService) {

    val funksjonsbrytere = listOf("familie.ef.soknad.feilsituasjon",
                                  "familie.ef.soknad.sprakvelger",
                                  "familie.ef.soknad.visSkalBehandlesINySaksbehandling")

    @GetMapping
    fun sjekkAlle(): Map<String, Boolean> {
        return funksjonsbrytere.associateWith { featureToggleService.isEnabled(it) }
    }

    @GetMapping("/{toggleId}")
    fun sjekkFunksjonsbryter(@PathVariable toggleId: String,
                             @RequestParam("defaultverdi") defaultVerdi: Boolean? = false): Boolean {
        return featureToggleService.isEnabled(toggleId, defaultVerdi ?: false)
    }
}
