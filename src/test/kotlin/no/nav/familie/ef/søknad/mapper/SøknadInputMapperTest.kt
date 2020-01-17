package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.SøknadInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class SøknadInputMapperTest {

    @Test
    @Disabled // Lag en ordentlig test når mapperen faktisk gjør noe.
    fun `mapTilIntern returner søknad basert på søknadDto`() {
        val søknadDto = SøknadInput("Dette er en søknad")
        val forventetSøknad = SøknadInput("Dette er en søknad")

        val søknad = SøknadMapper.mapTilIntern(søknadDto)

        assertThat(søknad).isEqualTo(forventetSøknad)
    }
}