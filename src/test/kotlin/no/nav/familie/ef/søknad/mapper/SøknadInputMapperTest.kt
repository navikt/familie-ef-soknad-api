package no.nav.familie.ef.søknad.mapper

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Person
import no.nav.familie.ef.søknad.mapper.kontrakt.PersonaliaMapper
import no.nav.familie.ef.søknad.mapper.kontrakt.SøknadMapper
import no.nav.familie.ef.søknad.mock.søkerMedDefaultVerdier
import no.nav.familie.ef.søknad.mock.søknadDto
import no.nav.familie.ef.søknad.service.DokumentService
import no.nav.familie.kontrakter.ef.søknad.Søknad
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

fun Søknad.getFødselsnummer(): String = personalia.verdi.fødselsnummer.verdi.verdi
fun Søknad.getSøkerNavn() = personalia.verdi.navn.verdi

internal class SøknadInputMapperTest {

    private val dokumentServiceServiceMock: DokumentService = mockk()
    private val mapper = SøknadMapper(dokumentServiceServiceMock)
    private val søknadDto = søknadDto()

    private val innsendingMottatt: LocalDateTime = LocalDateTime.now()

    @BeforeEach
    fun setUp() {
        every { dokumentServiceServiceMock.hentVedlegg(any()) } returns "DOKUMENTID123".toByteArray()
    }

    @Test
    fun `mapPersonalia mapper dto fra frontend til forventet Personalia`() {
        // When
        val personaliaFraSøknadDto = PersonaliaMapper.mapPersonalia(søknadDto)
        // Then
        assertThat(personaliaFraSøknadDto.toString()).isEqualTo(personalia().toString())
    }

    @Test
    fun `mapTilIntern returnerer dto med personnummer fra frontend`() {
        // Given
        val forventetFnr = "25058521089" //Syntetisk mocknummer
        val søknadDto = søknadDto.copy(person = Person(søker = søkerMedDefaultVerdier(forventetFnr = forventetFnr),
                                                       barn = søknadDto.person.barn))
        // When
        val søknad = mapper.mapTilIntern(søknadDto, innsendingMottatt).søknadMedVedlegg.søknad
        // Then
        assertThat(søknad.getFødselsnummer()).isEqualTo(forventetFnr)
    }

    @Test
    fun `map fra frontend`() {
        // Given
        val forventetNavn = "Hei Hopp"
        val søknadDto = søknadDto.copy(person = Person(barn = søknadDto.person.barn,
                                                       søker = søkerMedDefaultVerdier(forkortetNavn = forventetNavn)))
        // When
        val søknad = mapper.mapTilIntern(søknadDto, innsendingMottatt).søknadMedVedlegg.søknad
        // Then
        assertThat(søknad.getSøkerNavn()).isEqualTo(forventetNavn)
    }

    @Test
    fun `mapTilIntern skal fungere med telefonnummer som er null`() {
        // Given
        val søknadDto = søknadDto.copy(person = Person(søker = søkerMedDefaultVerdier(telefonnummer = null),
                                                       barn = søknadDto.person.barn))
        // When
        val søknad = mapper.mapTilIntern(søknadDto, innsendingMottatt).søknadMedVedlegg.søknad
        // Then
        val telefonnummer = søknad.personalia.verdi.telefonnummer
        assertThat(telefonnummer).isEqualTo(null)
    }

    @Test
    fun `mapTilIntern returnerer dto med riktig sivilstatus fra frontend`() {
        // Given
        val forventetSivilstatus = "Gift"
        val søknadDto = søknadDto.copy(person = Person(søker = søkerMedDefaultVerdier(sivilstatus = forventetSivilstatus),
                                                       barn = søknadDto.person.barn))
        // When
        val søknad = mapper.mapTilIntern(søknadDto, innsendingMottatt).søknadMedVedlegg.søknad
        // Then
        val sivilstatus = søknad.personalia.verdi.sivilstatus.verdi
        assertThat(sivilstatus).isEqualTo(forventetSivilstatus)
    }
}
