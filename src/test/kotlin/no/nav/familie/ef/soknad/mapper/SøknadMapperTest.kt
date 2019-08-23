package no.nav.familie.ef.soknad.mapper

import no.nav.familie.ef.soknad.api.dto.SøknadDto
import no.nav.familie.ef.soknad.integration.dto.Søknad
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SøknadMapperTest {

    @Test
    fun `mapTilIntern returner søknad basert på søknadDto`() {
        val søknadDto = SøknadDto("Dette er en søknad")
        val forventetSøknad = Søknad("Dette er en søknad")

        val søknad = SøknadMapper.mapTilIntern(søknadDto)

        assertThat(søknad).isEqualTo(forventetSøknad)
    }
}