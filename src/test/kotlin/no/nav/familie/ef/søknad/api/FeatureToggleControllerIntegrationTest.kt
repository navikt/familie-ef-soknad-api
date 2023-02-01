package no.nav.familie.ef.søknad.api

import io.getunleash.DefaultUnleash
import io.getunleash.UnleashContext
import io.getunleash.UnleashContextProvider
import io.getunleash.util.UnleashConfig
import no.nav.familie.ef.søknad.featuretoggle.ByEnvironmentStrategy
import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

@Ignore(
    "Krever at unleash kjører lokalt på port 4242, og at en funksjonsbryter " +
        "'test' er satt opp med byEnvironmentStrategy og miljø=local",
)
internal class FeatureToggleControllerIntegrationTest {

    val unleashUrl = "http://localhost:4242/api"

    private val unleashContextProvider = UnleashContextProvider {
        UnleashContext.builder()
            .environment("local")
            .appName("app")
            .build()
    }

    private val unleashService = object : FeatureToggleService {

        val unleash = DefaultUnleash(
            UnleashConfig.builder()
                .appName("app")
                .unleashAPI(unleashUrl)
                .unleashContextProvider(unleashContextProvider)
                .build(),
            ByEnvironmentStrategy(),
        )

        override fun isEnabled(toggleId: String, defaultValue: Boolean): Boolean {
            return unleash.isEnabled(toggleId, defaultValue)
        }
    }

    private val featureToggleController = FeatureToggleController(unleashService)

    @Test
    fun `skal funksjonsbryte på miljø`() {
        Assertions.assertThat(featureToggleController.sjekkFunksjonsbryter("test")).isTrue()
    }

    @Test
    fun `skal funksjonsbryte på miljø for ikke-definert bryter`() {
        Assertions.assertThat(featureToggleController.sjekkFunksjonsbryter("Ukjent", true)).isTrue()
    }
}
