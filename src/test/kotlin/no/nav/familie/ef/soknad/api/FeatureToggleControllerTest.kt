package no.nav.familie.ef.soknad.api

import no.finn.unleash.FakeUnleash
import no.nav.familie.ef.soknad.featuretoggle.UnleashServiceImpl
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class FeatureToggleControllerTest {
    private val fakeUnleash = FakeUnleash()

    private val featureToggleController = FeatureToggleController(UnleashServiceImpl.nyFraUnleash(fakeUnleash))

    @Test
    fun `skal svare true for påskrudde funksjonsbrytere`() {

        fakeUnleash.enable("bryter1")

        Assertions.assertThat(featureToggleController.sjekkFunksjonsbryter("bryter1",null)).isTrue()
        Assertions.assertThat(featureToggleController.sjekkFunksjonsbryter("bryter2",null)).isFalse()
    }

    @Test
    fun `skal gi standardverdi for manglende funksjonsbrytere`() {

        fakeUnleash.enable("bryter1")

        Assertions.assertThat(featureToggleController.sjekkFunksjonsbryter("bryter1",false)).isTrue()
        Assertions.assertThat(featureToggleController.sjekkFunksjonsbryter("bryter2",true)).isTrue()
    }

    @Test
    fun `skal funksjonsbryte på miljø`() {

        fakeUnleash.enable("bryter1")
        fakeUnleash.setVariant()

        Assertions.assertThat(featureToggleController.sjekkFunksjonsbryter("bryter1",false)).isTrue()
        Assertions.assertThat(featureToggleController.sjekkFunksjonsbryter("bryter2",true)).isTrue()
    }
}