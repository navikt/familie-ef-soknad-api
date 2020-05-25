package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.mapper.dokumentMap
import no.nav.familie.ef.søknad.mapper.falseOrNull
import no.nav.familie.ef.søknad.mock.søknadDto
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

val dokumenter = dokumentMap()

internal class BarnMapperTest {

    // Gitt
    private val søknadDto = søknadDto()

    // Når
    private val folkeregistrerteBarn = BarnMapper.mapFolkeregistrerteBarn(søknadDto.person.barn, dokumenter)
    private val nyregistrertBarn = BarnMapper.mapNyttBarn(søknadDto.person.barn, dokumenter)

    @Test
    fun `Mapping skal finne ett nytt barn`() {
        assertThat(nyregistrertBarn).hasSize(1)
    }

    @Test
    fun `Mapping skal finne ett folkeregistrert barn`() {
        assertThat(folkeregistrerteBarn).hasSize(1)
    }

    @Test
    fun `Folkeregistrert barn har riktig fødselsnummer`() {
        assertThat(folkeregistrerteBarn.first().fødselsnummer.verdi.verdi).isEqualTo("28021078036")
    }

    @Test
    fun `Folkeregistrert barn har riktig navn`() {
        assertThat(folkeregistrerteBarn.first().navn.verdi).isEqualTo("SHIBA INU")
    }

    @Test
    fun `Folkeregistrert barn har annenForelder`() {
        assertThat(folkeregistrerteBarn.first().annenForelder).isNotNull
    }

    @Test
    fun `Folkeregistrert barn har annenForelder bosatt i norge`() {
        val annenForelder = folkeregistrerteBarn.first().annenForelder?.verdi
        assertThat(annenForelder?.bosattNorge?.verdi).isEqualTo(true)
    }

    @Test
    fun `Folkeregistrert barn har annenForelder ikke oppgitt begrunnelse `() {
        val annenForelder = folkeregistrerteBarn.first().annenForelder?.verdi
        assertThat(annenForelder?.ikkeOppgittAnnenForelderBegrunnelse).isNull()
    }

    @Test
    fun `Folkeregistrert barn har annenForelder ikke oppgitt er false `() {
        val annenForelder = folkeregistrerteBarn.first().annenForelder?.verdi
        assertThat(annenForelder?.kanIkkeOppgiAnnenForelderFar?.verdi).isFalse()
    }

    @Test
    fun `Nyregistrert barn har annenForelder ikke oppgitt begrunnelse `() {
        val annenForelder = nyregistrertBarn.first().annenForelder?.verdi
        assertThat(annenForelder?.ikkeOppgittAnnenForelderBegrunnelse).isNotNull()
    }

    @Test
    fun `Nyregistrert barn har ikke oppgitt annen forelder`() {
        val annenForelder = nyregistrertBarn.first().annenForelder?.verdi
        assertThat(annenForelder?.kanIkkeOppgiAnnenForelderFar?.verdi).isTrue()
    }

    @Test
    fun `Nyregistrert barn må ha skalBarnetBoHosSøker `() {
        assertThat(nyregistrertBarn.first().skalBarnetBoHosSøker.verdi).isTrue()
    }

    @Test
    fun `Nyregistrert barn uten skalBarnBoHosDeg - skalBarnetBoHosSøker skal kaste exception `() {
        val barn = søknadDto.person.barn.filter { falseOrNull(it.lagtTil) }.first()
        val barnUtenBoHosDegVariabel = barn.copy(skalBarnBoHosDeg = null)
        assertThrows<IllegalStateException> { BarnMapper.mapNyttBarn(listOf(barnUtenBoHosDegVariabel), dokumenter) }
    }

    @Test
    fun `Forelder til folkeregistrert barn må ha samvær`() {
        val samvær = folkeregistrerteBarn.first().samvær!!.verdi
        assertThat(samvær.spørsmålAvtaleOmDeltBosted.verdi).isFalse()
        //assertThat(samvær.avtaleOmDeltBosted?.verdi).isFalse() //TODO vedlegg
        assertThat(samvær.skalAnnenForelderHaSamvær?.verdi).isEqualTo("Nei")
        assertThat(samvær.harDereSkriftligAvtaleOmSamvær?.verdi).isEqualTo("Nei")
        //assertThat(samvær.samværsavtale?.verdi).isFalse() //TODO vedlegg
        assertThat(samvær.hvordanPraktiseresSamværet?.verdi).isEqualTo("Praktiseres ikke")
        assertThat(samvær.borAnnenForelderISammeHus?.verdi).isEqualTo("Nei")
        assertThat(samvær.harDereTidligereBoddSammen?.verdi).isFalse()
        assertThat(samvær.nårFlyttetDereFraHverandre?.verdi).isEqualTo("2000-04-22")
        //assertThat(samvær.erklæringOmSamlivsbrudd?.verdi).isFalse() // TODO vedlegg
        assertThat(samvær.hvorMyeErDuSammenMedAnnenForelder?.verdi).isEqualTo("Vi møtes ikke")
        assertThat(samvær.beskrivSamværUtenBarn?.verdi)
                .isEqualTo("Vi møtes nå og da, gjerne for en tur i skogen eller et glass vin, hyggelig det.")
    }

}