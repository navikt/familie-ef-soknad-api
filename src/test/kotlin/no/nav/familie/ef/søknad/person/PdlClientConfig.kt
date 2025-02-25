package no.nav.familie.ef.søknad.person

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.person.dto.Bostedsadresse
import no.nav.familie.ef.søknad.person.dto.Familierelasjonsrolle
import no.nav.familie.ef.søknad.person.dto.ForelderBarnRelasjon
import no.nav.familie.ef.søknad.person.dto.Fødselsdato
import no.nav.familie.ef.søknad.person.dto.Historisk
import no.nav.familie.ef.søknad.person.dto.Navn
import no.nav.familie.ef.søknad.person.dto.PdlSøker
import no.nav.familie.ef.søknad.person.dto.Sivilstand
import no.nav.familie.ef.søknad.person.dto.Sivilstandstype
import no.nav.familie.ef.søknad.person.dto.Statsborgerskap
import no.nav.familie.ef.søknad.person.dto.Vegadresse
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

        every { pdlClient.ping() } returns Unit

        val fødselsdato = LocalDate.now().minusYears(30)
        every { pdlClient.hentSøker(any()) } returns
            PdlSøker(
                adressebeskyttelse = listOf(),
                bostedsadresse = bostedsadresse(),
                forelderBarnRelasjon = lagBarn(),
                navn = lagNavn(),
                sivilstand = sivilstand(),
                statsborgerskap = statsborgerskap(),
                fødselsdato = listOf(Fødselsdato(fødselsdato = fødselsdato, fødselsår = fødselsdato.year)),
            )

        return pdlClient
    }

    private fun lagBarn(): List<ForelderBarnRelasjon> =
        listOf(
            ForelderBarnRelasjon("28021078036", Familierelasjonsrolle.BARN),
            ForelderBarnRelasjon("19411890530", Familierelasjonsrolle.BARN),
        )

    private fun lagNavn(
        fornavn: String = "Fornavn",
        mellomnavn: String? = "mellomnavn",
        etternavn: String = "Etternavn",
    ): List<Navn> =
        listOf(
            Navn(
                fornavn,
                mellomnavn,
                etternavn,
            ),
        )

    private fun statsborgerskap(): List<Statsborgerskap> =
        listOf(
            Statsborgerskap(
                land = "NOR",
                gyldigFraOgMed = startdato,
                gyldigTilOgMed = null,
                metadata = Historisk(false),
            ),
            Statsborgerskap(
                land = "SWE",
                gyldigFraOgMed = startdato.minusYears(3),
                gyldigTilOgMed = startdato,
                metadata = Historisk(false),
            ),
        )

    private fun sivilstand(): List<Sivilstand> = listOf(Sivilstand(type = Sivilstandstype.SKILT))

    private fun bostedsadresse(): List<Bostedsadresse> =
        listOf(
            Bostedsadresse(
                vegadresse = vegadresse(),
                matrikkeladresse = null,
            ),
        )

    private fun vegadresse(): Vegadresse =
        Vegadresse(
            husnummer = "13",
            husbokstav = "b",
            adressenavn = "Charlies vei",
            postnummer = "0575",
            bruksenhetsnummer = "",
            matrikkelId = 1L,
        )
}
