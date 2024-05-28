package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.mock.søknadDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

val dokumenter = dokumentMap()

internal class BarnMapperTest {
    // Gitt
    private val søknadDto = søknadDto()

    // Når
    private val folkeregistrerteBarn = BarnMapper.map(søknadDto.person.barn, dokumenter).verdi.first()
    private val nyregistrertBarn = BarnMapper.map(søknadDto.person.barn, dokumenter).verdi[1]

    @Test
    fun `Folkeregistrert barn har riktig fødselsnummer`() {
        assertThat(folkeregistrerteBarn.fødselsnummer?.verdi?.verdi).isEqualTo("28021078036")
    }

    @Test
    fun `Folkeregistrert barn har riktig navn`() {
        assertThat(folkeregistrerteBarn.navn?.verdi).isEqualTo("SHIBA INU")
    }

    @Test
    fun `Folkeregistrert barn har annenForelder`() {
        assertThat(folkeregistrerteBarn.annenForelder).isNotNull
    }

    @Test
    fun `Folkeregistrert barn har annenForelder bosatt i norge`() {
        val annenForelder = folkeregistrerteBarn.annenForelder?.verdi
        assertThat(annenForelder?.bosattNorge?.verdi).isEqualTo(true)
    }

    @Test
    fun `Folkeregistrert barn har annenForelder ikke oppgitt begrunnelse `() {
        val annenForelder = folkeregistrerteBarn.annenForelder?.verdi
        assertThat(annenForelder?.ikkeOppgittAnnenForelderBegrunnelse).isNull()
    }

    @Test
    fun `Nyregistrert barn har annenForelder ikke oppgitt begrunnelse `() {
        val annenForelder = nyregistrertBarn.annenForelder?.verdi
        assertThat(annenForelder?.ikkeOppgittAnnenForelderBegrunnelse).isNotNull()
    }

    @Test
    fun `Nyregistrert barn må ha skalBarnetBoHosSøker `() {
        assertThat(nyregistrertBarn.harSkalHaSammeAdresse.verdi).isTrue()
    }

    @Test
    fun `Forelder til folkeregistrert barn må ha samvær`() {
        val samvær = folkeregistrerteBarn.samvær!!.verdi
        assertThat(samvær.spørsmålAvtaleOmDeltBosted?.verdi).isFalse()
        // assertThat(samvær.avtaleOmDeltBosted?.verdi).isFalse() //TODO vedlegg
        assertThat(samvær.skalAnnenForelderHaSamvær?.verdi).isEqualTo("Nei")
        assertThat(samvær.harDereSkriftligAvtaleOmSamvær?.verdi).isEqualTo("Nei")
        // assertThat(samvær.samværsavtale?.verdi).isFalse() //TODO vedlegg
        assertThat(samvær.hvordanPraktiseresSamværet?.verdi).isEqualTo("Praktiseres ikke")
        assertThat(samvær.borAnnenForelderISammeHus?.verdi).isEqualTo("Nei")
        assertThat(samvær.harDereTidligereBoddSammen?.verdi).isFalse()
        assertThat(samvær.nårFlyttetDereFraHverandre?.verdi).isEqualTo("2000-04-22")
        // assertThat(samvær.erklæringOmSamlivsbrudd?.verdi).isFalse() // TODO vedlegg
        assertThat(samvær.hvorMyeErDuSammenMedAnnenForelder?.verdi).isEqualTo("Vi møtes ikke")
        assertThat(samvær.beskrivSamværUtenBarn?.verdi)
            .isEqualTo("Vi møtes nå og da, gjerne for en tur i skogen eller et glass vin, hyggelig det.")
    }

    @Test
    fun `SærligeTilsynsbehov må ha verdi`() {
        assertThat(folkeregistrerteBarn.særligeTilsynsbehov?.verdi).isEqualTo("Har jo fort litt særlige tilsynsbehov da!")
    }
}
