package no.nav.familie.ef.søknad.mock

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.integration.PdlStsClient
import no.nav.familie.ef.søknad.integration.dto.pdl.Adressebeskyttelse
import no.nav.familie.ef.søknad.integration.dto.pdl.AdressebeskyttelseGradering.UGRADERT
import no.nav.familie.ef.søknad.integration.dto.pdl.BostedsadresseBarn
import no.nav.familie.ef.søknad.integration.dto.pdl.Familierelasjon
import no.nav.familie.ef.søknad.integration.dto.pdl.Familierelasjonsrolle
import no.nav.familie.ef.søknad.integration.dto.pdl.Fødsel
import no.nav.familie.ef.søknad.integration.dto.pdl.Navn
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlAnnenForelder
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlBarn
import no.nav.familie.ef.søknad.integration.dto.pdl.Vegadresse
import no.nav.familie.util.FnrGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import java.time.LocalDate

@Configuration
@Profile("mock-pdlStsClient")
class StsPdlClientConfig {


    @Bean
    @Primary
    fun pdlStsClient(): PdlStsClient {
        val pdlClient: PdlStsClient = mockk()
        val annenForelderFnr = FnrGenerator.generer()
        every { pdlClient.hentBarn(any()) } returns
                mapOf("28021078036" to PdlBarn(
                        adressebeskyttelse = listOf(Adressebeskyttelse(UGRADERT)),
                        bostedsadresse = bostedsadresseBarn(),
                        deltBosted = listOf(),
                        navn = lagNavn("Hei", "På", "Deg"),
                        fødsel = listOf(Fødsel(2000, LocalDate.now().minusMonths(6))),
                        dødsfall = listOf(),
                        familierelasjoner = listOf(Familierelasjon(annenForelderFnr, Familierelasjonsrolle.MEDMOR))
                ))

        every { pdlClient.hentAndreForeldre(any()) } returns
                mapOf(annenForelderFnr to PdlAnnenForelder(
                        adressebeskyttelse = listOf(Adressebeskyttelse(UGRADERT)),
                        navn = lagNavn("Bjørn", "Borg", "Borgersen"),
                        dødsfall = listOf(),
                ))

        return pdlClient
    }

    private fun lagNavn(fornavn: String = "Fornavn",
                        mellomnavn: String? = "mellomnavn",
                        etternavn: String = "Etternavn"): List<Navn> =
            listOf(Navn(fornavn,
                        mellomnavn,
                        etternavn))

    private fun bostedsadresseBarn(): List<BostedsadresseBarn> =
            listOf(BostedsadresseBarn(vegadresse = vegadresse(),
                                      matrikkeladresse = null))

    private fun vegadresse(): Vegadresse =
            Vegadresse(husnummer = "13",
                       husbokstav = "b",
                       adressenavn = "Charlies vei",
                       postnummer = "0575",
                       bruksenhetsnummer = "",
                       matrikkelId = 1L)
}