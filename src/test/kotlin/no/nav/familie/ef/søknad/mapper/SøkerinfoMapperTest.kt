package no.nav.familie.ef.søknad.mapper

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.api.dto.tps.Adresse
import no.nav.familie.ef.søknad.integration.dto.*
import no.nav.familie.ef.søknad.service.KodeverkService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class SøkerinfoMapperTest {

    private val kodeverkService = mockk<KodeverkService>(relaxed = true)
    private val søkerinfoMapper = SøkerinfoMapper(kodeverkService)

    @BeforeEach
    internal fun setUp() {
        every { kodeverkService.hentPoststed(any()) } returns "OSLO"
        every { kodeverkService.hentLand(any()) } returns "NORGE"
    }

    @Test
    fun `mapTilBarn konverterer alle felter i RelasjonDto fra TPS til rett felt i Barn`() {
        val relasjonDto = RelasjonDto("fødselsnummer",
                                      "Bob Kåre",
                                      15,
                                      null,
                                      LocalDate.of(2004, 4, 15),
                                      true)

        val barn = søkerinfoMapper.mapTilBarn(relasjonDto)

        assertThat(barn.fnr).isEqualTo("fødselsnummer")
        assertThat(barn.navn).isEqualTo("Bob Kåre")
        assertThat(barn.alder).isEqualTo(15)
        assertThat(barn.fødselsdato).isEqualTo(LocalDate.of(2004, 4, 15))
        assertThat(barn.harSammeAdresse).isEqualTo(true)
    }

    @Test
    fun `mapTilPerson konverterer alle felter i personinfoDto fra TPS til rett felt i Person`() {
        val personinfoDto = PersoninfoDto("fødselsnummer",
                                          NavnDto("Roy Tony"),
                                          AdresseinfoDto(BostedsadresseDto("Veien 24", "v/noen", "Oslo", "NOR", "0265")),
                                          DødsdatoDto(LocalDate.of(2024, 5, 16)),
                                          EgenansattDto(LocalDate.of(2017, 8, 30), true),
                                          KodeMedDatoOgKildeDto(KodeDto("kode")),
                                          InnvandringUtvandringDto(LocalDate.of(1999, 5, 4), LocalDate.of(2016, 8, 3)),
                                          KontonummerDto("8675309"),
                                          KodeMedDatoOgKildeDto(KodeDto("opphold")),
                                          KodeMedDatoOgKildeDto(KodeDto("GIFT")),
                                          KodeMedDatoOgKildeDto(KodeDto("bm")),
                                          KodeMedDatoOgKildeDto(KodeDto("NOR")),
                                          TelefoninfoDto("jobb", "mobil", "privat"))

        val person = søkerinfoMapper.mapTilPerson(personinfoDto)

        assertThat(person.fnr).isEqualTo("fødselsnummer")
        assertThat(person.forkortetNavn).isEqualTo("Roy Tony")
        assertThat(person.adresse).isEqualTo(Adresse("Veien 24", "0265", "OSLO"))
        assertThat(person.egenansatt).isEqualTo(true)
        assertThat(person.sivilstand).isEqualTo("GIFT")
        assertThat(person.statsborgerskap).isEqualTo("NORGE")

    }

    @Test
    fun `mapTilPerson konverterer alle null-felter i personinfoDto fra TPS til deafault-verdier i Person`() {
        val personinfoDto = PersoninfoDto("fødselsnummer",
                                          NavnDto("Roy Tony"),
                                          null,
                                          null,
                                          null,
                                          null,
                                          null,
                                          null,
                                          null,
                                          null,
                                          null,
                                          null,
                                          null)

        val person = søkerinfoMapper.mapTilPerson(personinfoDto)

        assertThat(person.fnr).isEqualTo("fødselsnummer")
        assertThat(person.forkortetNavn).isEqualTo("Roy Tony")
        assertThat(person.adresse).isEqualTo(Adresse("", "", ""))
        assertThat(person.egenansatt).isEqualTo(false)
        assertThat(person.sivilstand).isEqualTo("")
        assertThat(person.statsborgerskap).isEqualTo("")
    }

    @Test
    internal fun `ikke feile når henting av poststed feiler`() {
        every { kodeverkService.hentPoststed(any()) } throws RuntimeException("Feil")
        val bostedsadresse = BostedsadresseDto(null, null, null, null, "0575")
        val personinfoDto = PersoninfoDto("fødselsnummer",
                                          NavnDto("Roy Tony"),
                                          AdresseinfoDto(bostedsadresse = bostedsadresse),
                                          null,
                                          null,
                                          null,
                                          null,
                                          null,
                                          null,
                                          null,
                                          null,
                                          null,
                                          null)

        val person = søkerinfoMapper.mapTilPerson(personinfoDto)

        assertThat(person.adresse.postnummer).isEqualTo("0575")
        assertThat(person.adresse.poststed).isEqualTo("")
    }
}
