package no.nav.familie.ef.søknad.infrastruktur.featuretoggle

import no.nav.familie.unleash.UnleashService
import no.nav.security.token.support.core.api.Unprotected
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api/featuretoggle"], produces = [MediaType.APPLICATION_JSON_VALUE])
@Unprotected
class FeatureToggleController(
    private val unleashService: UnleashService,
) {
    private val featureTogglesIBruk =
        setOf(
            Toggle.NYNORSK,
            Toggle.NY_PDFKVITTERING,
            Toggle.HENT_SIST_INNSENDTE_SOKNAD_PER_STONAD,
        )

    @GetMapping
    fun sjekkAlle(): Map<String, Boolean> = featureTogglesIBruk.associate { it.toggleId to unleashService.isEnabled(it.toggleId) }

    @GetMapping("/{toggleId}")
    fun sjekkFunksjonsbryter(
        @PathVariable toggleId: String,
    ): Boolean {
        val toggle = Toggle.byToggleId(toggleId)
        return unleashService.isEnabled(toggle.toggleId)
    }
}
