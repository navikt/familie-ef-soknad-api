package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Adresse
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Person
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Søker
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.kontrakter.ef.søknad.Søknad
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

fun Søknad.getFødselsnummer(): String = personalia.verdi.fødselsnummer.verdi.verdi
fun Søknad.getSøkerNavn() = personalia.verdi.navn.verdi

internal class SøknadInputMapperTest {

    @Test
    fun `mapTilIntern returnerer dto med personnummer fra frontend`() {
        // Given
        val forventetFnr = "25058521089" //Syntetisk mocknummer
        val søknadDto = SøknadDto(person = Person(søker = søkerMedDefaultVerdier(forventetFnr = forventetFnr)))
        // When
        val søknad = SøknadMapper.mapTilIntern(søknadDto)
        // Then
        assertThat(søknad.getFødselsnummer()).isEqualTo(forventetFnr)
    }

    @Test
    fun `mapTilIntern returnerer dto med riktig navn fra frontend`() {
        // Given
        val forventetNavn = "Hei Hopp"
        val søknadDto = SøknadDto(person = Person(søker = søkerMedDefaultVerdier(forkortetNavn = forventetNavn)))
        // When
        val søknad = SøknadMapper.mapTilIntern(søknadDto)
        // Then
        assertThat(søknad.getSøkerNavn()).isEqualTo(forventetNavn)
    }

    @Test
    fun `mapTilIntern returnerer dto med riktig adresse fra frontend`() {
        // Given
        val forventetAdresse = Adresse("Motzfeldtsgate 10A", "0561") // TODO : legg til poststed og land i frontendDto ?
        val søknadDto = SøknadDto(person = Person(søker = søkerMedDefaultVerdier(adresse = forventetAdresse)))
        // When
        val søknad = SøknadMapper.mapTilIntern(søknadDto)
        // Then
        assertThat(søknad.personalia.verdi.adresse.verdi.adresse).isEqualTo(forventetAdresse.adresse)
    }

    @Test
    fun `mapTilIntern returnerer dto med riktig statsborgerskap fra frontend`() {
        // Given
        val forventetStatsborgerskap: String = "Svensk"
        val søknadDto = SøknadDto(person = Person(søker = søkerMedDefaultVerdier(statsborgerskap = forventetStatsborgerskap)))
        // When
        val søknad = SøknadMapper.mapTilIntern(søknadDto)
        // Then
        val statsborgerskap = søknad.personalia.verdi.statsborgerskap.verdi
        assertThat(statsborgerskap).isEqualTo(forventetStatsborgerskap)
    }

    @Test
    fun `mapTilIntern returnerer dto med riktig telefonnummer fra frontend`() {
        // Given
        val forventetTelefonnummer = "987654321"
        val søknadDto = SøknadDto(person = Person(søker = søkerMedDefaultVerdier(telefonnummer = forventetTelefonnummer)))
        // When
        val søknad = SøknadMapper.mapTilIntern(søknadDto)
        // Then
        val telefonnummer = søknad.personalia.verdi.telefonnummer?.verdi
        assertThat(telefonnummer).isEqualTo(forventetTelefonnummer)
    }

    @Test
    fun `mapTilIntern skal fungere med telefonnummer som er null`() {
        // Given
        val søknadDto = SøknadDto(person = Person(søker = søkerMedDefaultVerdier(telefonnummer = null)))
        // When
        val søknad = SøknadMapper.mapTilIntern(søknadDto)
        // Then
        val telefonnummer = søknad.personalia.verdi.telefonnummer
        assertThat(telefonnummer).isEqualTo(null)
    }

    @Test
    fun `mapTilIntern returnerer dto med riktig sivilstatus fra frontend`() {
        // Given
        val forventetSivilstatus = "Gift"
        val søknadDto = SøknadDto(person = Person(søker = søkerMedDefaultVerdier(sivilstatus = forventetSivilstatus)))
        // When
        val søknad = SøknadMapper.mapTilIntern(søknadDto)
        // Then
        val sivilstatus = søknad.personalia.verdi.sivilstatus.verdi
        assertThat(sivilstatus).isEqualTo(forventetSivilstatus)
    }
}

fun søkerMedDefaultVerdier(forventetFnr: String = "19128449828",
                           forkortetNavn: String = "Snill Veps",
                           adresse: Adresse = Adresse("", ""),
                           statsborgerskap: String = "Norsk",
                           telefonnummer: String? = null,
                           sivilstatus: String = "Gift")
        = Søker(fnr = forventetFnr,
                forkortetNavn = forkortetNavn,
                adresse = adresse,
                statsborgerskap = statsborgerskap,
                telefonnummer = telefonnummer,
                sivilstand = sivilstatus

)