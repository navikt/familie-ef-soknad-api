package no.nav.familie.ef.søknad.person

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.person.dto.Adressebeskyttelse
import no.nav.familie.ef.søknad.person.dto.AdressebeskyttelseGradering.UGRADERT
import no.nav.familie.ef.søknad.person.dto.BostedsadresseBarn
import no.nav.familie.ef.søknad.person.dto.Familierelasjonsrolle
import no.nav.familie.ef.søknad.person.dto.ForelderBarnRelasjon
import no.nav.familie.ef.søknad.person.dto.Fødsel
import no.nav.familie.ef.søknad.person.dto.Navn
import no.nav.familie.ef.søknad.person.dto.PdlAnnenForelder
import no.nav.familie.ef.søknad.person.dto.PdlBarn
import no.nav.familie.ef.søknad.person.dto.Vegadresse
import no.nav.familie.util.FnrGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import java.time.LocalDate

@Configuration
@Profile("mock-pdlApp2AppClient")
class PdlApp2AppClientConfig {
    @Bean
    @Primary
    fun pdlApp2AppClient(): PdlApp2AppClient {
        val pdlApp2AppClient: PdlApp2AppClient = mockk()
        val medforelderFnr = FnrGenerator.generer(år = 1999)
        every { pdlApp2AppClient.hentBarn(any()) } returns
            mapOf(
                "28021078036" to
                    PdlBarn(
                        adressebeskyttelse = listOf(Adressebeskyttelse(UGRADERT)),
                        bostedsadresse = bostedsadresseBarn(),
                        deltBosted = listOf(),
                        navn = lagNavn("Hei", "På", "Deg"),
                        fødsel = listOf(Fødsel(2000, LocalDate.now().minusMonths(6))),
                        dødsfall = listOf(),
                        forelderBarnRelasjon = listOf(ForelderBarnRelasjon(medforelderFnr, Familierelasjonsrolle.MEDMOR)),
                    ),
            )

        every { pdlApp2AppClient.hentAndreForeldre(any()) } returns
            mapOf(
                medforelderFnr to
                    PdlAnnenForelder(
                        adressebeskyttelse = listOf(Adressebeskyttelse(UGRADERT)),
                        navn = lagNavn("Bjørn", "Borg", "Borgersen"),
                        dødsfall = listOf(),
                    ),
            )

        return pdlApp2AppClient
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

    private fun bostedsadresseBarn(): List<BostedsadresseBarn> =
        listOf(
            BostedsadresseBarn(
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
