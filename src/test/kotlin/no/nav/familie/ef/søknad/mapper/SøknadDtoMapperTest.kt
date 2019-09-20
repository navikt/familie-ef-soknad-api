package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.Søknad
import no.nav.familie.ef.søknad.integration.dto.SøknadDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SøknadDtoMapperTest {

    @Test
    fun `mapTilIntern returner søknad basert på søknadDto`() {
        val søknadDto = Søknad("Dette er en søknad")
        val forventetSøknad = SøknadDto("Dette er en søknad")

        val søknad = SøknadMapper.mapTilIntern(søknadDto)

        assertThat(søknad).isEqualTo(forventetSøknad)
    }
}