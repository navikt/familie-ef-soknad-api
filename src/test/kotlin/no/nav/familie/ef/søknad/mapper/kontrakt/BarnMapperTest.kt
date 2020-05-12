package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.mapper.falseOrNull
import no.nav.familie.ef.søknad.mock.søknadDto
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class BarnMapperTest {

    // Gitt
    private val søknadDto = søknadDto()

    // Når
    private val folkeregistrerteBarn = BarnMapper.mapFolkeregistrerteBarn(søknadDto.person.barn)
    private val nyregistrertBarn = BarnMapper.mapNyttBarn(søknadDto.person.barn)

    @Test
    fun `Mapping skal finne ett nytt barn`() {
        Assertions.assertThat(nyregistrertBarn).hasSize(1)
    }

    @Test
    fun `Mapping skal finne ett folkeregistrert barn`() {
        Assertions.assertThat(folkeregistrerteBarn).hasSize(1)
    }

    @Test
    fun `Folkeregistrert barn har riktig fødselsnummer`() {
        Assertions.assertThat(folkeregistrerteBarn.first().fødselsnummer.verdi.verdi).isEqualTo("28021078036")
    }

    @Test
    fun `Folkeregistrert barn har riktig navn`() {
        Assertions.assertThat(folkeregistrerteBarn.first().navn.verdi).isEqualTo("SHIBA INU")
    }

    @Test
    fun `Folkeregistrert barn har annenForelder`() {
        Assertions.assertThat(folkeregistrerteBarn.first().annenForelder).isNotNull
    }

    @Test
    fun `Folkeregistrert barn har annenForelder med riktig addresse`() {
        val annenForelder = folkeregistrerteBarn.first().annenForelder?.verdi
        Assertions.assertThat(annenForelder?.adresse).isEqualTo(null)
    }

    @Test
    fun `Folkeregistrert barn har annenForelder bosatt i norge`() {
        val annenForelder = folkeregistrerteBarn.first().annenForelder?.verdi
        Assertions.assertThat(annenForelder?.bosattNorge?.verdi).isEqualTo(true)
    }

    @Test
    fun `Folkeregistrert barn har annenForelder ikke oppgitt begrunnelse `() {
        val annenForelder = folkeregistrerteBarn.first().annenForelder?.verdi
        Assertions.assertThat(annenForelder?.ikkeOppgittAnnenForelderBegrunnelse).isNull()
    }

    @Test
    fun `Folkeregistrert barn har annenForelder ikke oppgitt er false `() {
        val annenForelder = folkeregistrerteBarn.first().annenForelder?.verdi
        Assertions.assertThat(annenForelder?.kanIkkeOppgiAnnenForelderFar?.verdi).isFalse()
    }

    @Test
    fun `Nyregistrert barn har annenForelder ikke oppgitt begrunnelse `() {
        val annenForelder = nyregistrertBarn.first().annenForelder?.verdi
        Assertions.assertThat(annenForelder?.ikkeOppgittAnnenForelderBegrunnelse).isNotNull()
    }

    @Test
    fun `Nyregistrert barn har ikke oppgitt annen forelder`() {
        val annenForelder = nyregistrertBarn.first().annenForelder?.verdi
        Assertions.assertThat(annenForelder?.kanIkkeOppgiAnnenForelderFar?.verdi).isTrue()
    }

    @Test
    fun `Nyregistrert barn må ha skalBarnetBoHosSøker `() {
        Assertions.assertThat(nyregistrertBarn.first().skalBarnetBoHosSøker.verdi).isTrue()
    }

    @Test
    fun `Nyregistrert barn uten skalBarnBoHosDeg - skalBarnetBoHosSøker skal kaste exception `() {
        val barn = søknadDto.person.barn.filter { falseOrNull(it.lagtTil) }.first()
        val barnUtenBoHosDegVariabel = barn.copy(skalBarnBoHosDeg = null)
        assertThrows<IllegalStateException> { BarnMapper.mapNyttBarn(listOf(barnUtenBoHosDegVariabel)) }
    }

}