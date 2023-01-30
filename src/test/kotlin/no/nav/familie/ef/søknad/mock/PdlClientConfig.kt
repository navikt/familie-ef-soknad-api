package no.nav.familie.ef.søknad.mock

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.integration.PdlClient
import no.nav.familie.ef.søknad.integration.dto.pdl.Bostedsadresse
import no.nav.familie.ef.søknad.integration.dto.pdl.Familierelasjonsrolle
import no.nav.familie.ef.søknad.integration.dto.pdl.ForelderBarnRelasjon
import no.nav.familie.ef.søknad.integration.dto.pdl.Navn
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlSøker
import no.nav.familie.ef.søknad.integration.dto.pdl.Sivilstand
import no.nav.familie.ef.søknad.integration.dto.pdl.Sivilstandstype
import no.nav.familie.ef.søknad.integration.dto.pdl.Statsborgerskap
import no.nav.familie.ef.søknad.integration.dto.pdl.Vegadresse
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

        every { pdlClient.hentSøker(any()) } returns
            PdlSøker(
                adressebeskyttelse = listOf(),
                bostedsadresse = bostedsadresse(),
                forelderBarnRelasjon = lagBarn(),
                navn = lagNavn(),
                sivilstand = sivilstand(),
                statsborgerskap = statsborgerskap(),
            )

        return pdlClient
    }

    private fun lagBarn(): List<ForelderBarnRelasjon> {
        return listOf(ForelderBarnRelasjon("28021078036", Familierelasjonsrolle.BARN))
    }

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
            ),
            Statsborgerskap(
                land = "SWE",
                gyldigFraOgMed = startdato.minusYears(3),
                gyldigTilOgMed = startdato,
            ),
        )

    private fun sivilstand(): List<Sivilstand> =
        listOf(Sivilstand(type = Sivilstandstype.SKILT))

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
