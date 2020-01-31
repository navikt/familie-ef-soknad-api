package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.SøknadInput
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Person
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Søker
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
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

    @Test
    fun `mapTilIntern returnerer dto med input fra frontend`() {
        val forventetFnr = "25058521089" //Syntetisk mocknummer
        val søknadDto = SøknadDto(person = Person(søker = Søker(fnr = forventetFnr)))
        val søknad = SøknadMapper.mapTilIntern(søknadDto)

        assertThat(søknad.personalia.verdi.fødselsnummer.verdi.verdi).isEqualTo(forventetFnr)
    }
}