package no.nav.familie.ef.søknad.person.mapper

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import no.nav.familie.ef.søknad.kodeverk.KodeverkService
import no.nav.familie.ef.søknad.person.dto.Adressebeskyttelse
import no.nav.familie.ef.søknad.person.dto.AdressebeskyttelseGradering
import no.nav.familie.ef.søknad.person.dto.AdressebeskyttelseGradering.FORTROLIG
import no.nav.familie.ef.søknad.person.dto.AdressebeskyttelseGradering.STRENGT_FORTROLIG
import no.nav.familie.ef.søknad.person.dto.AdressebeskyttelseGradering.STRENGT_FORTROLIG_UTLAND
import no.nav.familie.ef.søknad.person.dto.AdressebeskyttelseGradering.UGRADERT
import no.nav.familie.ef.søknad.person.dto.Bostedsadresse
import no.nav.familie.ef.søknad.person.dto.BostedsadresseBarn
import no.nav.familie.ef.søknad.person.dto.DeltBosted
import no.nav.familie.ef.søknad.person.dto.Familierelasjonsrolle
import no.nav.familie.ef.søknad.person.dto.ForelderBarnRelasjon
import no.nav.familie.ef.søknad.person.dto.Fødsel
import no.nav.familie.ef.søknad.person.dto.Matrikkeladresse
import no.nav.familie.ef.søknad.person.dto.MatrikkeladresseBarn
import no.nav.familie.ef.søknad.person.dto.Navn
import no.nav.familie.ef.søknad.person.dto.PdlAnnenForelder
import no.nav.familie.ef.søknad.person.dto.PdlBarn
import no.nav.familie.ef.søknad.person.dto.PdlSøker
import no.nav.familie.ef.søknad.person.dto.Sivilstand
import no.nav.familie.ef.søknad.person.dto.Sivilstandstype.UOPPGITT
import no.nav.familie.ef.søknad.person.dto.Vegadresse
import no.nav.familie.ef.søknad.person.dto.visningsnavn
import no.nav.familie.sikkerhet.EksternBrukerUtils
import no.nav.familie.util.FnrGenerator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Period

internal class SøkerinfoMapperTest {
    private val kodeverkService = mockk<KodeverkService>(relaxed = true)
    private val søkerinfoMapper = SøkerinfoMapper(kodeverkService)

    private val barn =
        barn().copy(
            fødsel = listOf(Fødsel(LocalDate.now().year, LocalDate.now())),
            navn = listOf(Navn("fornavn", "", "Etternavn")),
        )

    @BeforeEach
    internal fun setUp() {
        every { kodeverkService.hentPoststed(any()) } returns "OSLO"
        every { kodeverkService.hentLand(any()) } returns "NORGE"
        mockkObject(EksternBrukerUtils)
        every { EksternBrukerUtils.hentFnrFraToken() } returns FnrGenerator.generer()
    }

    @AfterEach
    internal fun tearDown() {
        unmockkObject(EksternBrukerUtils)
    }

    @Test
    fun `skal mappe strengt fortrolig`() {
        listOf(
            Pair(pdlSøker(), false),
            Pair(pdlSøker(UGRADERT), false),
            Pair(pdlSøker(FORTROLIG), false),
            Pair(pdlSøker(STRENGT_FORTROLIG), true),
            Pair(pdlSøker(STRENGT_FORTROLIG_UTLAND), true),
        ).forEach {
            val søkerinfo = søkerinfoMapper.mapTilSøkerinfo(it.first, emptyMap(), emptyMap())
            assertThat(søkerinfo.søker.erStrengtFortrolig).isEqualTo(it.second)
        }
    }

    @Test
    fun `Test visningsnavn`() {
        val navn1 = Navn("Roy", "", "Toy").visningsnavn()
        val navn2 = Navn("Roy", null, "Toy").visningsnavn()
        val navn3 = Navn("Roy", "Tull", "Toy").visningsnavn()
        assertThat(navn1).isEqualTo("Roy Toy")
        assertThat(navn2).isEqualTo("Roy Toy")
        assertThat(navn3).isEqualTo("Roy Tull Toy")
    }

    @Test
    fun `MedForelder alder og navn`() {
        // Gitt
        val navn = Navn("Roy", "", "Toy")
        val medforelderFortrolig = PdlAnnenForelder(listOf(Adressebeskyttelse(FORTROLIG)), listOf(), listOf(navn))
        val ident = FnrGenerator.generer(år = 1999)
        // Når
        val annenForelder = medforelderFortrolig.tilDto(ident)
        // Da vil
        assertThat(annenForelder.harAdressesperre).isTrue
        assertThat(annenForelder.alder).isGreaterThan(0)
        assertThat(annenForelder.navn).isNotEqualTo("Roy Toy")
        assertThat(annenForelder.navn).isEqualTo("")
    }

    @Test
    fun `AnnenForelder adressebeskyttelse fortrolig mappes til harAdressesperre`() {
        val navn = Navn("Roy", "", "Toy")
        val annenForelderFortrolig = PdlAnnenForelder(listOf(Adressebeskyttelse(FORTROLIG)), listOf(), listOf(navn))
        val annenForelderStrengtFortrolig =
            PdlAnnenForelder(listOf(Adressebeskyttelse(STRENGT_FORTROLIG)), listOf(), listOf(navn))
        val annenForelderStrengtFortroligUtland =
            PdlAnnenForelder(listOf(Adressebeskyttelse(STRENGT_FORTROLIG_UTLAND)), listOf(), listOf(navn))
        //
        val ident = FnrGenerator.generer(år = 1999)
        val annenForelder = annenForelderFortrolig.tilDto(ident)
        assertThat(annenForelder.harAdressesperre).isTrue
        assertThat(annenForelderStrengtFortrolig.tilDto(ident).harAdressesperre).isTrue
        assertThat(annenForelderStrengtFortroligUtland.tilDto(ident).harAdressesperre).isTrue
    }

    @Test
    fun `AnnenForelder adressebeskyttelse UGRADERT skal ikke ha adressesperre`() {
        val navn = Navn("Roy", "", "Toy")
        val pdlAnnenForelder = PdlAnnenForelder(listOf(Adressebeskyttelse(UGRADERT)), listOf(), listOf(navn))
        //
        val tilDto = pdlAnnenForelder.tilDto(FnrGenerator.generer())
        //
        assertThat(tilDto.harAdressesperre == false)
    }

    @Test
    fun `AnnenForelder adressebeskyttelse tom skal ikke ha adressesperre`() {
        val navn = Navn("Roy", "", "Toy")

        val pdlAnnenForelder = PdlAnnenForelder(listOf(), listOf(), listOf(navn))
        //
        val tilDto = pdlAnnenForelder.tilDto(FnrGenerator.generer())
        //
        assertThat(tilDto.harAdressesperre == false)
    }

    @Test
    fun `skal kalkulere alder riktig for en person født i 1990 med D-nummer`() {
        val fødselsdato = LocalDate.of(1990, 1, 1)
        val fødselsnummer = FnrGenerator.generer(fødselsdato, erDnummer = true)
        every { EksternBrukerUtils.hentFnrFraToken() } returns fødselsnummer

        val pdlSøker = opprettPdlSøker()
        val person = søkerinfoMapper.mapTilSøkerinfo(pdlSøker, emptyMap(), emptyMap()).søker
        val forventetAlder = Period.between(fødselsdato, LocalDate.now()).years

        assertThat(person.alder).isEqualTo(forventetAlder)
    }

    @Test
    fun `skal kalkulere alder riktig for en person født i 1990`() {
        val fødselsdato = LocalDate.of(1990, 1, 1)
        val fødselsnummer = FnrGenerator.generer(fødselsdato)
        every { EksternBrukerUtils.hentFnrFraToken() } returns fødselsnummer

        val pdlSøker = opprettPdlSøker()
        val person = søkerinfoMapper.mapTilSøkerinfo(pdlSøker, emptyMap(), emptyMap()).søker
        val forventetAlder = Period.between(fødselsdato, LocalDate.now()).years

        assertThat(person.alder).isEqualTo(forventetAlder)
    }

    @Test
    fun `skal kalkulere alder riktig for en person født i 2000`() {
        val fødselsdato = LocalDate.of(2000, 1, 1)
        val fødselsnummer = FnrGenerator.generer(fødselsdato)
        every { EksternBrukerUtils.hentFnrFraToken() } returns fødselsnummer

        val pdlSøker = opprettPdlSøker()
        val person = søkerinfoMapper.mapTilSøkerinfo(pdlSøker, emptyMap(), emptyMap()).søker
        val forventetAlder = Period.between(fødselsdato, LocalDate.now()).years

        assertThat(person.alder).isEqualTo(forventetAlder)
    }

    @Test
    fun `skal kalkulere at alder er nøyaktig 20 år`() {
        val fødselsdato = LocalDate.now().minusYears(20)
        val fødselsnummer = FnrGenerator.generer(fødselsdato)
        every { EksternBrukerUtils.hentFnrFraToken() } returns fødselsnummer

        val pdlSøker = opprettPdlSøker()
        val person = søkerinfoMapper.mapTilSøkerinfo(pdlSøker, emptyMap(), emptyMap()).søker

        assertThat(person.alder).isEqualTo(20)
    }

    @Test
    fun `AnnenForelder mappes til barn`() {
        val pdlSøker =
            opprettPdlSøker()

        val navn = Navn("Roy", "", "Toy")
        val relatertPersonsIdent = FnrGenerator.generer()
        val barn =
            barn().copy(
                fødsel = listOf(Fødsel(LocalDate.now().year, LocalDate.now())),
                navn = listOf(Navn("Boy", "", "Moy")),
                forelderBarnRelasjon =
                    listOf(
                        ForelderBarnRelasjon(
                            relatertPersonsIdent,
                            Familierelasjonsrolle.FAR,
                        ),
                    ),
            )
        val adressebeskyttelse = Adressebeskyttelse(UGRADERT)
        val pdlAnnenForelder = PdlAnnenForelder(listOf(adressebeskyttelse), listOf(), listOf(navn))
        val andreForeldre = mapOf(relatertPersonsIdent to pdlAnnenForelder)
        val person = søkerinfoMapper.mapTilSøkerinfo(pdlSøker, mapOf("999" to barn), andreForeldre)
        assertThat(
            person.barn
                .first()
                .medforelder
                ?.navn,
        ).isEqualTo("Roy Toy")
    }

    @Test
    fun `AnnenForelder en annen forelder, to barn mappes til riktig barn`() {
        // Gitt
        val pdlSøker =
            opprettPdlSøker()

        val navn = Navn("Roy", "", "Toy")
        val relatertPersonsIdent = FnrGenerator.generer()
        val barn =
            barn().copy(
                fødsel = listOf(Fødsel(LocalDate.now().year, LocalDate.now())),
                navn = listOf(Navn("Boy", "", "Moy")),
                forelderBarnRelasjon =
                    listOf(
                        ForelderBarnRelasjon(
                            relatertPersonsIdent,
                            Familierelasjonsrolle.FAR,
                        ),
                    ),
            )
        val barn2 =
            barn().copy(
                fødsel = listOf(Fødsel(LocalDate.now().year, LocalDate.now())),
                navn = listOf(Navn("Boy", "", "Moy")),
                forelderBarnRelasjon = listOf(),
            )

        val adressebeskyttelse = Adressebeskyttelse(UGRADERT)
        val pdlAnnenForelder = PdlAnnenForelder(listOf(adressebeskyttelse), listOf(), listOf(navn))
        val andreForeldre = mapOf(relatertPersonsIdent to pdlAnnenForelder)
        // når
        val person = søkerinfoMapper.mapTilSøkerinfo(pdlSøker, mapOf("999" to barn, "888" to barn2), andreForeldre)
        // da skal
        val barnDto = person.barn.filter { it.fnr.equals("999") }
        val barn2Dto = person.barn.filter { it.fnr.equals("888") }
        assertThat(barnDto.first().medforelder).isNotNull
        assertThat(barn2Dto.first().medforelder).isNull()
    }

    private fun opprettPdlSøker(): PdlSøker {
        val pdlSøker =
            PdlSøker(
                listOf(),
                listOf(),
                listOf(),
                navn = listOf(Navn("fornavn", "mellomnavn", "etternavn")),
                sivilstand = listOf(Sivilstand(UOPPGITT)),
                listOf(),
            )
        return pdlSøker
    }

    @Test
    fun `sivilstand er tom liste skal mappes til UOPPGITT`() {
        // Gitt
        val pdlSøker =
            PdlSøker(
                listOf(),
                listOf(),
                listOf(),
                navn = listOf(Navn("fornavn", "mellomnavn", "etternavn")),
                sivilstand = listOf(),
                listOf(),
            )
        // når
        val person = søkerinfoMapper.mapTilSøkerinfo(pdlSøker, mapOf("999" to barn), mapOf())
        // da skal
        assertThat(person.søker.sivilstand).isEqualTo(UOPPGITT.toString())
    }

    @Test
    internal fun `harSammeAdresse - tester ulike varianter av nullverdier`() {
        assertThat(søkerinfoMapper.harSammeAdresse(null, barn()))
            .withFailMessage("Søkers adresse er null")
            .isFalse
        assertThat(søkerinfoMapper.harSammeAdresse(bostedsadresse(), barn(bostedsadresseBarn(vegadresse(1)))))
            .withFailMessage("Søker har ikke vegadresse eller matrikkeladresse")
            .isFalse
        assertThat(søkerinfoMapper.harSammeAdresse(bostedsadresse(vegadresse(1)), barn()))
            .withFailMessage("Barnet har ikke vegadresse eller matrikkeladresse")
            .isFalse
    }

    @Test
    internal fun `harSammeAdresse - skal teste vegadresse`() {
        assertThat(
            søkerinfoMapper.harSammeAdresse(
                bostedsadresse(vegadresse(matrikkelId = 1)),
                barn(bostedsadresseBarn(vegadresse(matrikkelId = 1))),
            ),
        ).withFailMessage("MatrikkelId er lik på vegadresse")
            .isTrue
        assertThat(
            søkerinfoMapper.harSammeAdresse(
                bostedsadresse(vegadresse(adressenavn = "1")),
                barn(bostedsadresseBarn(vegadresse(adressenavn = "1"))),
            ),
        ).withFailMessage("Har samme adressenavn")
            .isTrue
        assertThat(
            søkerinfoMapper.harSammeAdresse(
                bostedsadresse(vegadresse(adressenavn = "1")),
                barn(bostedsadresseBarn(vegadresse(adressenavn = "2"))),
            ),
        ).withFailMessage("Har ulike adressenavn")
            .isFalse
    }

    @Test
    internal fun `har ikke samme bostedadresse, men har delt bosted`() {
        val datoEtterIdag = LocalDate.now().plusDays(1)
        val datoFørIdag = LocalDate.now().minusDays(1)
        val barnAdresse = bostedsadresseBarn(vegadresse(matrikkelId = 1))

        // Guard
        assertThat(
            søkerinfoMapper.harSammeAdresse(
                bostedsadresse(vegadresse(matrikkelId = 2)),
                barn(barnAdresse),
            ),
        ).withFailMessage("MatrikkelId er lik på vegadresse")
            .isFalse
        // Har delt adresse -> forventer true
        assertThat(
            søkerinfoMapper.harSammeAdresse(
                bostedsadresse(vegadresse(matrikkelId = 2)),
                barn(barnAdresse, DeltBosted(datoFørIdag, datoEtterIdag)),
            ),
        ).withFailMessage("har delt adresse")
            .isTrue
    }

    @Test
    internal fun `harSammeAdresse - skal teste matrikkeladresse`() {
        assertThat(
            søkerinfoMapper.harSammeAdresse(
                bostedsadresse(matrikkeladresse = matrikkeladresse(1)),
                barn(bostedsadresseBarn(matrikkeladresse = matrikkeladresseBarn(1))),
            ),
        ).withFailMessage("MatrikkelId er lik på matrikkelId")
            .isTrue
        assertThat(
            søkerinfoMapper.harSammeAdresse(
                bostedsadresse(matrikkeladresse = matrikkeladresse(null)),
                barn(bostedsadresseBarn(matrikkeladresse = matrikkeladresseBarn(null))),
            ),
        ).isFalse
    }

    @Test
    internal fun `har samme adresse men har delt bosted`() {
        val søkersAdresse = bostedsadresse(vegadresse(matrikkelId = 1))
        val barnAdresse = bostedsadresseBarn(vegadresse(matrikkelId = 1))
        val datoEtterIdag = LocalDate.now().plusDays(1)
        val datoFørIdag = LocalDate.now().minusDays(1)
        assertThat(
            søkerinfoMapper.harSammeAdresse(
                søkersAdresse,
                barn(barnAdresse, DeltBosted(datoEtterIdag, null)),
            ),
        ).withFailMessage("Delt bosted frem i tid")
            .isTrue
        assertThat(
            søkerinfoMapper.harSammeAdresse(
                søkersAdresse,
                barn(barnAdresse, DeltBosted(datoFørIdag, datoFørIdag)),
            ),
        ).withFailMessage("Delt bosted avsluttet")
            .isTrue

        assertThat(
            søkerinfoMapper.harSammeAdresse(
                søkersAdresse,
                barn(barnAdresse, DeltBosted(datoFørIdag, datoEtterIdag)),
            ),
        ).withFailMessage("Har delt bosted med sluttdato frem i tid")
            .isTrue

        assertThat(
            søkerinfoMapper.harSammeAdresse(
                søkersAdresse,
                barn(barnAdresse, DeltBosted(datoFørIdag, null)),
            ),
        ).withFailMessage("Har delt bosted med sluttdato null")
            .isTrue
    }

    private fun pdlSøker(
        adressebeskyttelse: AdressebeskyttelseGradering? = null,
        navn: Navn = Navn("Roy", "", "Toy"),
    ): PdlSøker =
        PdlSøker(
            adressebeskyttelse = adressebeskyttelse?.let { listOf(Adressebeskyttelse(it)) } ?: emptyList(),
            bostedsadresse = listOf(),
            forelderBarnRelasjon = listOf(),
            navn = listOf(navn),
            sivilstand = listOf(),
            statsborgerskap = listOf(),
        )

    private fun vegadresse(
        matrikkelId: Long? = null,
        adressenavn: String? = null,
    ) = Vegadresse(
        null,
        null,
        null,
        adressenavn,
        null,
        matrikkelId,
    )

    private fun bostedsadresse(
        vegadresse: Vegadresse? = null,
        matrikkeladresse: Matrikkeladresse? = null,
    ) = Bostedsadresse(vegadresse, matrikkeladresse)

    private fun bostedsadresseBarn(
        vegadresse: Vegadresse? = null,
        matrikkeladresse: MatrikkeladresseBarn? = null,
    ) = BostedsadresseBarn(vegadresse, matrikkeladresse)

    private fun matrikkeladresse(matrikkelId: Long?) = Matrikkeladresse(matrikkelId, "", "0572")

    private fun matrikkeladresseBarn(matrikkelId: Long?) = MatrikkeladresseBarn(matrikkelId)

    private fun barn(
        bostedsadresse: BostedsadresseBarn? = null,
        deltBosted: DeltBosted? = null,
    ) = PdlBarn(
        emptyList(),
        bostedsadresse?.let { listOf(it) } ?: emptyList(),
        deltBosted?.let { listOf(it) } ?: emptyList(),
        emptyList(),
        emptyList(),
        emptyList(),
        emptyList(),
    )
}
