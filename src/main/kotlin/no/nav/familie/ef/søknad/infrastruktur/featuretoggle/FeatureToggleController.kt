package no.nav.familie.ef.søknad.infrastruktur.featuretoggle

import no.nav.security.token.support.core.api.Unprotected
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api/featuretoggle"], produces = [MediaType.APPLICATION_JSON_VALUE])
@Unprotected
class FeatureToggleController(
    private val featureToggleService: FeatureToggleService,
) {
    val funksjonsbrytere =
        listOf(
            "familie.ef.soknad.feilsituasjon",
            "familie.ef.soknad.nynorsk",
            "familie.ef.soknad-ny-pdfkvittering",
        )

    @GetMapping
    fun sjekkAlle(): Map<String, Boolean> = funksjonsbrytere.associateWith { featureToggleService.isEnabled(it) }

    @GetMapping("/{toggleId}")
    fun sjekkFunksjonsbryter(
        @PathVariable toggleId: String,
        @RequestParam("defaultverdi") defaultVerdi: Boolean? = false,
    ): Boolean = featureToggleService.isEnabled(toggleId, defaultVerdi ?: false)
}
