package no.nav.familie.ef.søknad.api

import no.finn.unleash.FakeUnleash
import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class FeatureToggleControllerTest {
    private val fakeUnleash = FakeUnleash()

    private val fakeUnleashService = object : FeatureToggleService {
        override fun isEnabled(toggleId: String, defaultValue: Boolean): Boolean {
            return fakeUnleash.isEnabled(toggleId, defaultValue)
        }
    }

    private val featureToggleController = FeatureToggleController(fakeUnleashService)

    @Test
    fun `skal svare true for påskrudde funksjonsbrytere`() {

        fakeUnleash.enable("bryter1")

        Assertions.assertThat(featureToggleController.sjekkFunksjonsbryter("bryter1", null)).isTrue()
        Assertions.assertThat(featureToggleController.sjekkFunksjonsbryter("bryter2", null)).isFalse()
    }

    @Test
    fun `skal gi standardverdi for manglende funksjonsbrytere`() {

        fakeUnleash.enable("bryter1")

        Assertions.assertThat(featureToggleController.sjekkFunksjonsbryter("bryter1", false)).isTrue()
        Assertions.assertThat(featureToggleController.sjekkFunksjonsbryter("bryter2", true)).isTrue()
    }

    @Test
    fun `skal funksjonsbryte på miljø`() {

        fakeUnleash.enable("bryter1")

        Assertions.assertThat(featureToggleController.sjekkFunksjonsbryter("bryter1", false)).isTrue()
        Assertions.assertThat(featureToggleController.sjekkFunksjonsbryter("bryter2", true)).isTrue()
    }
}
