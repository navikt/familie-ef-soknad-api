package no.nav.familie.ef.søknad.søknad.domain

import no.nav.familie.kontrakter.ef.iverksett.SvarId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SvarIdValidatorTest {
    @Test
    fun `requireSvarIdIfPresent for BooleanFelt`() {
        val booleanFelt = BooleanFelt("Bor du på denne adressen?", true, null)
        assertThat(booleanFelt.requireSvarIdIfPresent()).isNull()

        val booleanFeltMedSvarId = BooleanFelt("Bor du på denne adressen?", true, SvarId.JA.name)
        assertThat(booleanFeltMedSvarId.requireSvarIdIfPresent()).isEqualTo(true)

        val booleanFeltMedSvarIdSomIkkeFinnes = BooleanFelt("Bor du på denne adressen?", true, "null")
        assertThat(booleanFeltMedSvarIdSomIkkeFinnes.requireSvarIdIfPresent()).isEqualTo(false)
    }

    @Test
    fun `requireSvarIdIfPresent for TekstFelt`() {
        val tekstFelt = TekstFelt("Hvorfor er du alene med barn?", "Samlivsbrudd med den andre forelderen", null)
        assertThat(tekstFelt.harGyldigSvarId()).isNull()

        val tekstFeltMedSvarId = TekstFelt("Bor du på denne adressen?", "Samlivsbrudd med den andre forelderen", "JA")
        assertThat(tekstFeltMedSvarId.tilSvarIdOrNull()).isEqualTo(SvarId.JA)

        val tekstFeltMedSvarIdSomIkkeFinnes = TekstFelt("Bor du på denne adressen?", "Samlivsbrudd med den andre forelderen", "null")
        assertThat(tekstFeltMedSvarIdSomIkkeFinnes.harGyldigSvarId()).isEqualTo(false)
    }
}
