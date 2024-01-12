package no.nav.familie.ef.søknad.featuretoggle

import io.getunleash.UnleashContext
import no.nav.familie.ef.søknad.infrastruktur.featuretoggle.ByEnvironmentStrategy
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class ByEnvironmentStrategyTest {
    private val map = ByEnvironmentStrategy.lagPropertyMapMedMiljø("p", "q")

    @Test
    fun `skal svare true for riktig miljø`() {
        Assertions.assertThat(
            ByEnvironmentStrategy().isEnabled(map, UnleashContext.builder().environment("p").build()),
        ).isTrue()
    }

    @Test
    fun `skal svare false for påskrudde funksjonsbrytere`() {
        Assertions.assertThat(
            ByEnvironmentStrategy().isEnabled(map, UnleashContext.builder().environment("l").build()),
        ).isFalse()
    }
}
