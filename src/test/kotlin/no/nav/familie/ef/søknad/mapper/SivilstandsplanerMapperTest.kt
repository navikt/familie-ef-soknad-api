package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Bosituasjon
import no.nav.familie.ef.søknad.mapper.kontrakt.BosituasjonMapper
import no.nav.familie.ef.søknad.mapper.kontrakt.SivilstandsplanerMapper
import no.nav.familie.ef.søknad.mock.søknadDto
import no.nav.familie.kontrakter.ef.søknad.Fødselsnummer
import no.nav.familie.kontrakter.felles.objectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDate

internal class SivilstandsplanerMapperTest {

    private val bosituasjonGifteplaner = getBosituasjon("sivilstandsplaner.json")
    private val dokumenter = emptyMap<String, DokumentasjonWrapper>()

    @Test
    fun `Vi mapper sivilstandsplaner verdi false`() {
        // Given
        val bosituasjonUtenGifteplaner = søknadDto().bosituasjon
        // When
        val sivilstandsplaner = SivilstandsplanerMapper.mapSivilstandsplaner(bosituasjonUtenGifteplaner)
        // Then
        assertThat(sivilstandsplaner.harPlaner?.verdi).isEqualTo(false)
    }

    @Test
    fun `Vi mapper sivilstandsplaner til verdi true`() {
        // When
        val sivilstandsplaner = SivilstandsplanerMapper.mapSivilstandsplaner(bosituasjonGifteplaner)
        // Then
        assertThat(sivilstandsplaner.harPlaner?.verdi).isEqualTo(true)
    }

    @Test
    fun `Vi mapper sivilstandsplaner label`() {
        // When
        val sivilstandsplaner = SivilstandsplanerMapper.mapSivilstandsplaner(bosituasjonGifteplaner)
        // Then
        assertThat(sivilstandsplaner.harPlaner?.label).isEqualTo("Har du konkrete planer om å gifte deg eller bli samboer?")
    }


    @Test
    fun `Vi mapper dato for sambo- gifteplaner `() {
        // When
        val sivilstandsplaner = SivilstandsplanerMapper.mapSivilstandsplaner(bosituasjonGifteplaner)
        // Then
        assertThat(sivilstandsplaner.fraDato?.verdi).isEqualTo(LocalDate.of(2020, 3, 26))
    }

    @Test
    fun `Vi mapper label for sambo- gifteplaner `() {
        // When
        val sivilstandsplaner = SivilstandsplanerMapper.mapSivilstandsplaner(bosituasjonGifteplaner)
        // Then
        assertThat(sivilstandsplaner.fraDato?.label).isEqualTo("Når skal dette skje?")
    }

    @Test
    fun `Vi mapper vordende samboers navn`() {
        // When
        val samboerdetaljer = BosituasjonMapper.mapBosituasjon(bosituasjonGifteplaner, dokumenter).samboerdetaljer
        // Then
        assertThat(samboerdetaljer?.verdi?.navn?.verdi).isEqualTo("Giflteklar Navnesen")
    }

    @Test
    fun `Vi mapper vordende samboers  personnummer`() {
        // When
        val samboerdetaljer = BosituasjonMapper.mapBosituasjon(bosituasjonGifteplaner, dokumenter).samboerdetaljer
        // Then
        assertThat(samboerdetaljer?.verdi?.fødselsnummer?.verdi).isEqualTo(Fødselsnummer("26077624804"))
    }

    @Test
    fun `Vi mapper vordende samboers fødselsdato`() {
        // When
        val samboerdetaljer = BosituasjonMapper.mapBosituasjon(bosituasjonGifteplaner, dokumenter).samboerdetaljer
        // Then
        assertThat(samboerdetaljer?.verdi?.fødselsdato?.verdi).isEqualTo(LocalDate.of(1976, 7, 26))
    }

    private fun getBosituasjon(fileName: String) = objectMapper.readValue(File("src/test/resources/$fileName"),
                                                                          Bosituasjon::class.java)
}
