package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Person
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Søker
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SøknadInputMapperTest {

    @Test
    fun `mapTilIntern returnerer dto med input fra frontend`() {
        val forventetFnr = "25058521089" //Syntetisk mocknummer
        val søknadDto = SøknadDto(person = Person(søker = Søker(fnr = forventetFnr)))
        val søknad = SøknadMapper.mapTilIntern(søknadDto)

        assertThat(søknad.personalia.verdi.fødselsnummer.verdi.verdi).isEqualTo(forventetFnr)
    }
}