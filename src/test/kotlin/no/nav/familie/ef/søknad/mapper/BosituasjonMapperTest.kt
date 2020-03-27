package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Bosituasjon
import no.nav.familie.ef.søknad.mapper.kontrakt.BosituasjonMapper
import no.nav.familie.ef.søknad.mock.søknadDto
import no.nav.familie.kontrakter.ef.søknad.Fødselsnummer
import no.nav.familie.kontrakter.felles.objectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDate

internal class BosituasjonMapperTest {

    private val bosituasjon = søknadDto().bosituasjon
    private val bositiasjonSamboer = getBosituasjon("bositiasjonSamboer.json")


    @Test
    fun `Vi mapper bossituasjon verdi`() {
        // When
        val bosituasjonMapped = BosituasjonMapper.mapBosituasjon(bosituasjon)
        // Then
        assertThat(bosituasjonMapped.delerDuBolig.verdi).isEqualTo("Nei, jeg bor alene med barn eller jeg er gravid og bor alene")
    }

    @Test
    fun `Vi mapper bossituasjon label`() {
        // When
        val bosituasjonMapped = BosituasjonMapper.mapBosituasjon(bosituasjon)
        // Then
        assertThat(bosituasjonMapped.delerDuBolig.label).isEqualTo("Deler du bolig med andre voksne?")
    }

    @Test
    fun `Vi mapper bossituasjon med samboer deler bolig verdi`() {
        // When
        val bosituasjonMapped = BosituasjonMapper.mapBosituasjon(bositiasjonSamboer)
        // Then
        assertThat(bosituasjonMapped.delerDuBolig.verdi).isEqualTo("Ja, jeg har samboer og lever i et ekteskapslignende forhold")
    }

    @Test
    fun `Vi mapper bossituasjon med samboer deler bolig dato`() {
        // When
        val bosituasjonMapped = BosituasjonMapper.mapBosituasjon(bositiasjonSamboer)
        // Then
        assertThat(bosituasjonMapped.sammenflyttingsdato?.verdi).isEqualTo(LocalDate.of(2020 ,3 ,26))
    }

    @Test
    fun `Vi mapper bossituasjon med samboer deler bolig samboer`() {
        // When
        val bosituasjonMapped = BosituasjonMapper.mapBosituasjon(bositiasjonSamboer)
        // Then
        assertThat(bosituasjonMapped.samboerdetaljer?.verdi?.navn?.verdi).isEqualTo("Bob Dyland")
    }

    @Test
    fun `Vi mapper bossituasjon med samboer deler bolig samboer har personnummer`() {
        // When
        val bosituasjonMapped = BosituasjonMapper.mapBosituasjon(bositiasjonSamboer)
        // Then
        assertThat(bosituasjonMapped.samboerdetaljer?.verdi?.fødselsnummer?.verdi).isEqualTo(Fødselsnummer("26077624804"))
    }

    @Test
    fun `Vi mapper bossituasjon med samboer deler bolig samboer har fødselsdato`() {
        // When
        val bosituasjonMapped = BosituasjonMapper.mapBosituasjon(bositiasjonSamboer)
        // Then
        assertThat(bosituasjonMapped.samboerdetaljer?.verdi?.fødselsdato?.verdi).isEqualTo(LocalDate.of(1976,7 , 26))
    }


    private fun getBosituasjon(fileName : String) = objectMapper.readValue(File("src/test/resources/$fileName"),
                                                                           Bosituasjon::class.java)
}