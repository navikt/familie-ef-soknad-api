package no.nav.familie.ef.søknad.mock

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.integration.PdlClient
import no.nav.familie.ef.søknad.integration.dto.pdl.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import java.time.LocalDate

@Configuration
@Profile("mock-pdl")
class PdlClientConfig {

    private val startdato = LocalDate.of(2020, 1, 1)
    private val sluttdato = LocalDate.of(2021, 1, 1)


    @Bean
    @Primary
    fun pdlClient(): PdlClient {
        val pdlClient: PdlClient = mockk()

        every { pdlClient.hentSøker(any()) } returns
                PdlSøker(
                        adressebeskyttelse = listOf(Adressebeskyttelse(gradering = AdressebeskyttelseGradering.UGRADERT)),
                        bostedsadresse = bostedsadresse(),
                        familierelasjoner = listOf(),
                        folkeregisterpersonstatus = listOf(Folkeregisterpersonstatus("bosatt", "bosattEtterFolkeregisterloven")),
                        navn = lagNavn(),
                        sivilstand = sivilstand(),
                        statsborgerskap = statsborgerskap(),
                        tilrettelagtKommunikasjon = listOf(),
                )

        return pdlClient
    }

    private fun lagNavn(fornavn: String = "Fornavn",
                        mellomnavn: String? = "mellomnavn",
                        etternavn: String = "Etternavn"): List<Navn> =
            listOf(Navn(fornavn,
                        mellomnavn,
                        etternavn))

    private fun statsborgerskap(): List<Statsborgerskap> =
            listOf(Statsborgerskap(land = "NOR",
                                   gyldigFraOgMed = startdato,
                                   gyldigTilOgMed = null),
                   Statsborgerskap(land = "SWE",
                                   gyldigFraOgMed = startdato.minusYears(3),
                                   gyldigTilOgMed = startdato))

    private fun sivilstand(): List<Sivilstand> =
            listOf(Sivilstand(type = Sivilstandstype.SKILT,
                              gyldigFraOgMed = startdato,
                              myndighet = "Myndighet",
                              kommune = "0301",
                              sted = "Oslo",
                              utland = null,
                              relatertVedSivilstand = "11111122222",
                              bekreftelsesdato = "2020-01-01"))


    private fun bostedsadresse(): List<Bostedsadresse> =
            listOf(Bostedsadresse(angittFlyttedato = startdato,
                                  coAdressenavn = "CONAVN",
                                  vegadresse = vegadresse(),
                                  ukjentBosted = null))

    private fun vegadresse(): Vegadresse =
            Vegadresse(husnummer = "13",
                       husbokstav = "b",
                       adressenavn = "Charlies vei",
                       kommunenummer = "0301",
                       postnummer = "0575",
                       bruksenhetsnummer = "",
                       tilleggsnavn = null,
                       matrikkelId = 1L)
}