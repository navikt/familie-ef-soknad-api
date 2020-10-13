package no.nav.familie.ef.søknad.mock

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.sak.integration.dto.pdl.*
import no.nav.familie.ef.søknad.integration.PdlClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import java.time.LocalDate
import java.time.LocalDateTime

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
                        dødsfall = listOf(),
                        familierelasjoner = listOf(),
                        fødsel = listOf(),
                        folkeregisterpersonstatus = listOf(Folkeregisterpersonstatus("bosatt", "bosattEtterFolkeregisterloven")),
                        fullmakt = fullmakter(),
                        kjønn = lagKjønn(KjønnType.KVINNE),
                        kontaktadresse = kontaktadresse(),
                        navn = lagNavn(),
                        opphold = listOf(Opphold(Oppholdstillatelse.PERMANENT, startdato, null)),
                        oppholdsadresse = listOf(),
                        sivilstand = sivilstand(),
                        statsborgerskap = statsborgerskap(),
                        telefonnummer = listOf(Telefonnummer(landskode = "+47", nummer = "98999923", prioritet = 1)),
                        tilrettelagtKommunikasjon = listOf(),
                        innflyttingTilNorge = listOf(InnflyttingTilNorge("SWE", "Stockholm")),
                        utflyttingFraNorge = listOf(UtflyttingFraNorge("SWE", "Stockholm")),
                        vergemaalEllerFremtidsfullmakt = listOf()
                )

        return pdlClient
    }

    private val folkeregistermetadata = Folkeregistermetadata(LocalDateTime.now(), LocalDateTime.now())

    private fun lagKjønn(kjønnType: KjønnType = KjønnType.KVINNE) = listOf(Kjønn(kjønnType))

    private fun lagNavn(fornavn: String = "Fornavn",
                        mellomnavn: String? = "mellomnavn",
                        etternavn: String = "Etternavn"): List<Navn> =
            listOf(Navn(fornavn,
                        mellomnavn,
                        etternavn,
                        Metadata(endringer = listOf(MetadataEndringer(LocalDate.now())))))

    private fun kontaktadresse(): List<Kontaktadresse> =
            listOf(Kontaktadresse(coAdressenavn = "co",
                                  gyldigFraOgMed = startdato,
                                  gyldigTilOgMed = sluttdato,
                                  postadresseIFrittFormat = null,
                                  postboksadresse = null,
                                  type = KontaktadresseType.INNLAND,
                                  utenlandskAdresse = null,
                                  utenlandskAdresseIFrittFormat = null,
                                  vegadresse = vegadresse()))

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

    private fun fullmakter(): List<Fullmakt> =
            listOf(Fullmakt(gyldigTilOgMed = startdato,
                            gyldigFraOgMed = sluttdato,
                            motpartsPersonident = "11111133333",
                            motpartsRolle = MotpartsRolle.FULLMEKTIG,
                            omraader = listOf()))

    private fun bostedsadresse(): List<Bostedsadresse> =
            listOf(Bostedsadresse(angittFlyttedato = startdato,
                                  folkeregistermetadata = Folkeregistermetadata(gyldighetstidspunkt = LocalDateTime.now(),
                                                                                opphørstidspunkt = startdato.atStartOfDay()),
                                  utenlandskAdresse = null,
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
                       koordinater = null)
}