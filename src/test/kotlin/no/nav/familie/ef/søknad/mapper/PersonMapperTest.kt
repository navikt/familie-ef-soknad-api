package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.tps.Adresse
import no.nav.familie.ef.søknad.integration.dto.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class PersonMapperTest {

    @Test
    fun `mapTilBarn konverterer alle felter i RelasjonDto til rett felt i Barn`() {
        val relasjonDto = RelasjonDto("fødselsnummer",
                                      "Bob Kåre",
                                      15,
                                      null,
                                      LocalDate.of(2004, 4, 15),
                                      true)

        val barn = PersonMapper.mapTilBarn(relasjonDto)

        assertThat(barn.fnr).isEqualTo("fødselsnummer")
        assertThat(barn.navn).isEqualTo("Bob Kåre")
        assertThat(barn.alder).isEqualTo(15)
        assertThat(barn.fødselsdato).isEqualTo(LocalDate.of(2004, 4, 15))
        assertThat(barn.harSammeAdresse).isEqualTo(true)
    }

    @Test
    fun `mapTilPerson konverterer alle felter i personinfoDto til rett felt i Person`() {
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
                                          KodeMedDatoOgKildeDto(KodeDto("NO")),
                                          TelefoninfoDto("jobb", "mobil", "privat"))

        val person = PersonMapper.mapTilPerson(personinfoDto)

        assertThat(person.fnr).isEqualTo("fødselsnummer")
        assertThat(person.forkortetNavn).isEqualTo("Roy Tony")
        assertThat(person.adresse).isEqualTo(Adresse("Veien 24", "v/noen", "Oslo", "0265"))
        assertThat(person.egenansatt).isEqualTo(true)
        assertThat(person.sivilstand).isEqualTo("GIFT")
        assertThat(person.statsborgerskap).isEqualTo("NO")

    }

    @Test
    fun `mapTilPerson konverterer alle null-felter i personinfoDto til deafault-verdier i Person`() {
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

        val person = PersonMapper.mapTilPerson(personinfoDto)

        assertThat(person.fnr).isEqualTo("fødselsnummer")
        assertThat(person.forkortetNavn).isEqualTo("Roy Tony")
        assertThat(person.adresse).isEqualTo(Adresse("", "", "", ""))
        assertThat(person.egenansatt).isEqualTo(false)
        assertThat(person.sivilstand).isEqualTo("")
        assertThat(person.statsborgerskap).isEqualTo("")
    }


}