package no.nav.familie.ef.søknad.infrastruktur.featuretoggle

import no.nav.familie.unleash.DefaultUnleashService
import org.springframework.stereotype.Service

@Service
class FeatureToggleService(
    val defaultUnleashService: DefaultUnleashService,
) {
    fun isEnabled(toggleId: String): Boolean = defaultUnleashService.isEnabled(toggleId)

    fun isEnabled(
        toggleId: String,
        defaultValue: Boolean,
    ): Boolean = defaultUnleashService.isEnabled(toggleId, defaultValue)
}
