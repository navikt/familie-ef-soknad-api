package no.nav.familie.ef.søknad.api

import no.finn.unleash.DefaultUnleash
import no.finn.unleash.UnleashContext
import no.finn.unleash.UnleashContextProvider
import no.finn.unleash.util.UnleashConfig
import no.nav.familie.ef.søknad.featuretoggle.ByEnvironmentStrategy
import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class FeatureToggleControllerIntegratiobTest {

    private val unleashContextProvider = object : UnleashContextProvider {
        override fun getContext(): UnleashContext {
            return UnleashContext.builder()
                    .environment("local")
                    .appName("app")
                    .build();
        }
    }

    private val unleashService = object : FeatureToggleService {

        val unleash = DefaultUnleash(UnleashConfig.builder()
                .appName("app")
                .unleashAPI("http://localhost:4242/api")
                .unleashContextProvider(unleashContextProvider)
                .build(), ByEnvironmentStrategy())

        override fun isEnabled(toggleId: String, defaultValue: Boolean): Boolean {
            return unleash.isEnabled(toggleId,defaultValue);
        }
    }

    private val featureToggleController = FeatureToggleController(unleashService)

    @Test
    fun `skal funksjonsbryte på miljø`() {

        Assertions.assertThat(featureToggleController.sjekkFunksjonsbryter("test")).isTrue()
    }

    @Test
    fun `skal funksjonsbryte på miljø for ikke-definert bryter`() {

        Assertions.assertThat(featureToggleController.sjekkFunksjonsbryter("Ukjent",true)).isTrue()
    }

}