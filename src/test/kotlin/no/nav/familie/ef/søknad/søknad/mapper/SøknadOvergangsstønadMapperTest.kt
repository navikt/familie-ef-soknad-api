package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.mock.søkerMedDefaultVerdier
import no.nav.familie.ef.søknad.mock.søknadDto
import no.nav.familie.ef.søknad.søknad.domain.Person
import no.nav.familie.kontrakter.ef.søknad.SøknadOvergangsstønad
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

fun SøknadOvergangsstønad.getFødselsnummer(): String = personalia.verdi.fødselsnummer.verdi.verdi
fun SøknadOvergangsstønad.getSøkerNavn() = personalia.verdi.navn.verdi

internal class SøknadOvergangsstønadMapperTest {

    private val mapper = SøknadOvergangsstønadMapper()
    private val søknadDto = søknadDto()

    private val innsendingMottatt: LocalDateTime = LocalDateTime.now()

    @Test
    fun `mapPersonalia mapper dto fra frontend til forventet Personalia`() {
        // When
        val personaliaFraSøknadDto = PersonaliaMapper.map(søknadDto.person.søker).verdi
        // Then
        assertThat(personaliaFraSøknadDto.toString()).isEqualTo(personalia().toString())
    }

    @Test
    fun `mapTilIntern returnerer dto med personnummer fra frontend`() {
        // Given
        val forventetFnr = "25058521089" // Syntetisk mocknummer
        val søknadDto = søknadDto.copy(
            person = Person(
                søker = søkerMedDefaultVerdier(forventetFnr = forventetFnr),
                barn = søknadDto.person.barn,
            ),
        )
        // When
        val søknad = mapper.mapTilIntern(søknadDto, innsendingMottatt).søknad
        // Then
        assertThat(søknad.getFødselsnummer()).isEqualTo(forventetFnr)
    }

    @Test
    fun `map fra frontend`() {
        // Given
        val forventetNavn = "Hei Hopp"
        val søknadDto = søknadDto.copy(
            person = Person(
                barn = søknadDto.person.barn,
                søker = søkerMedDefaultVerdier(forkortetNavn = forventetNavn),
            ),
        )
        // When
        val søknad = mapper.mapTilIntern(søknadDto, innsendingMottatt).søknad
        // Then
        assertThat(søknad.getSøkerNavn()).isEqualTo(forventetNavn)
    }

    @Test
    fun `mapTilIntern returnerer dto med riktig sivilstatus fra frontend`() {
        // Given
        val forventetSivilstatus = "Gift"
        val søknadDto = søknadDto.copy(
            person = Person(
                søker = søkerMedDefaultVerdier(sivilstatus = forventetSivilstatus),
                barn = søknadDto.person.barn,
            ),
        )
        // When
        val søknad = mapper.mapTilIntern(søknadDto, innsendingMottatt).søknad
        // Then
        val sivilstatus = søknad.personalia.verdi.sivilstatus.verdi
        assertThat(sivilstatus).isEqualTo(forventetSivilstatus)
    }
}
