package no.nav.familie.ef.søknad.featuretoggle

import no.finn.unleash.UnleashContext
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class ByEnvironmentStrategyTest {
    val map = ByEnvironmentStrategy.miljø("p","q")

    @Test
    fun `skal svare true for riktig miljø`() {

        Assertions.assertThat(
                ByEnvironmentStrategy().isEnabled(map, UnleashContext.builder().environment("p").build())
        ).isTrue();
    }

    @Test
    fun `skal svare false for påskrudde funksjonsbrytere`() {

        Assertions.assertThat(
                ByEnvironmentStrategy().isEnabled(map, UnleashContext.builder().environment("l").build())
        ).isFalse();
    }

    @Test()
    fun feiler() {
        // Må fjernes. Skal fanges opp av maven
        Assertions.assertThat(true).isFalse()
    }

}