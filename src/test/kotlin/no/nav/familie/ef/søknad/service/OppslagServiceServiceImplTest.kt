package no.nav.familie.ef.søknad.service

import io.mockk.*
import no.nav.familie.ef.søknad.api.ApiFeil
import no.nav.familie.ef.søknad.api.dto.pdl.Adresse
import no.nav.familie.ef.søknad.api.dto.pdl.Person
import no.nav.familie.ef.søknad.config.RegelverkConfig
import no.nav.familie.ef.søknad.integration.PdlClient
import no.nav.familie.ef.søknad.integration.PdlStsClient
import no.nav.familie.ef.søknad.integration.dto.AdresseinfoDto
import no.nav.familie.ef.søknad.integration.dto.PersoninfoDto
import no.nav.familie.ef.søknad.integration.dto.pdl.*
import no.nav.familie.ef.søknad.integration.dto.pdl.AdressebeskyttelseGradering.FORTROLIG
import no.nav.familie.ef.søknad.integration.dto.pdl.AdressebeskyttelseGradering.UGRADERT
import no.nav.familie.ef.søknad.mapper.SøkerinfoMapper
import no.nav.familie.sikkerhet.EksternBrukerUtils
import no.nav.familie.util.FnrGenerator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class OppslagServiceServiceImplTest {

    val pdlClient: PdlClient = mockk()
    val regelverkConfig: RegelverkConfig = RegelverkConfig(RegelverkConfig.Alder(18))
    val pdlStsClient: PdlStsClient = mockk()

    //private val søkerinfoMapper = spyk(SøkerinfoMapper(mockk(relaxed = true)))
    private val søkerinfoMapper = spyk(SøkerinfoMapper(mockk(relaxed = true)))

    private val oppslagServiceService = OppslagServiceServiceImpl(
        pdlClient,
        pdlStsClient, regelverkConfig, søkerinfoMapper
    )

    @BeforeEach
    fun setUp() {
        mockkObject(EksternBrukerUtils)
        every { EksternBrukerUtils.hentFnrFraToken() } returns "12345678911"
        mockPdlHentBarn()
        mockHentPersonPdlClient()
        every { pdlStsClient.hentAndreForeldre(any()) } returns (mapOf())
    }

    @Test
    fun `Lik søkerInfo skal ha lik hash`() {
        mockPdlHentBarn()
        mockHentPersonPdlClient("Et navn")
        val søkerinfo = oppslagServiceService.hentSøkerinfo()
        mockHentPersonPdlClient("Et navn")

        val søkerinfo2 = oppslagServiceService.hentSøkerinfo()
        assertEquals(søkerinfo.hash, søkerinfo2.hash)
    }

    @Test
    fun `Lik søkerInfo med adressesperre skal ha lik hash`() {
        mockPdlHentBarn(adressebeskyttelseGradering = FORTROLIG)
        mockHentPersonPdlClient(fornavn = "Et navn", adressebeskyttelseGradering = FORTROLIG)
        val søkerinfo = oppslagServiceService.hentSøkerinfo()
        mockHentPersonPdlClient(fornavn = "Et navn", adressebeskyttelseGradering = FORTROLIG)
        val søkerinfo2 = oppslagServiceService.hentSøkerinfo()
        assertEquals(søkerinfo.hash, søkerinfo2.hash)
    }

    @Test
    fun `Lik søker uten adressesperre skal ikke kunne hente info når relatert person har adressesperre`() {
        mockPdlHentBarn(adressebeskyttelseGradering = FORTROLIG)
        mockHentPersonPdlClient(fornavn = "Et navn")
        mockHentPersonPdlClient(fornavn = "Et navn")
        val ex = assertThrows<ApiFeil> { oppslagServiceService.hentSøkerinfo() }
        assertThat(ex.feil).isEqualTo("Personer med tilknytning til søker har adressesperre")
        assertThat(ex.httpStatus).isEqualTo(org.springframework.http.HttpStatus.FORBIDDEN)
    }

    @Test
    fun `SøkerInfo med ulike navn skal ikke ha lik hash`() {
        val søkerinfo = oppslagServiceService.hentSøkerinfo()
        mockHentPersonPdlClient("Et annet navn")
        val søkerinfo2 = oppslagServiceService.hentSøkerinfo()
        assertNotEquals(søkerinfo.hash, søkerinfo2.hash)
    }

    @Test
    fun `SøkerInfo med endring i barn skal ikke ha lik hash`() {
        mockHentPersonPdlClient()
        mockPdlHentBarn("FørsteNavn")
        val søkerinfo = oppslagServiceService.hentSøkerinfo()
        mockPdlHentBarn("AnnetBarnenavn")
        val søkerinfo2 = oppslagServiceService.hentSøkerinfo()
        assertNotEquals(søkerinfo.hash, søkerinfo2.hash)
    }

    // TODO test hash av person med barn med annen forelder (med fortrolig adresse?)

    @Test
    fun `SøkerInfo med endring i familierelasjoner skal ikke ha lik hash`() {
        mockHentPersonPdlClient()
        mockPdlHentBarn("navn")
        val søkerinfo = oppslagServiceService.hentSøkerinfo()
        val pdlBarn = pdlBarn()
        //  mockPdlHentBarn()
        val generer = FnrGenerator.generer()
        val copy = pdlBarn.second.copy(
            familierelasjoner = listOf(Familierelasjon(generer, Familierelasjonsrolle.FAR)),
            navn = listOf(Navn("navn", "navn", "navn"))
        )
        every { pdlStsClient.hentBarn(any()) } returns (mapOf(pdlBarn.first to copy))
        every { pdlStsClient.hentAndreForeldre(any()) } returns mapOf(
            generer to PdlAnnenForelder(
                listOf(),
                listOf(),
                listOf(
                    Navn(
                        "forelder",
                        "forelder",
                        "forelder"
                    )
                )
            )
        )

        val søkerinfo2 = oppslagServiceService.hentSøkerinfo()
        assertNotEquals(søkerinfo.hash, søkerinfo2.hash)
    }


    @Test
    fun `Test filtrering på dødsdato`() {
        assertThat(oppslagServiceService.erILive(pdlBarn(dødsfall = Dødsfall(LocalDate.MIN)).second)).isFalse
        assertThat(oppslagServiceService.erILive(pdlBarn().second)).isTrue
    }

    @Test
    fun `er i aktuell alder`() {
        assertThat(oppslagServiceService.erIAktuellAlder(fødselsdato = LocalDate.now())).isTrue
        assertThat(
            oppslagServiceService.erIAktuellAlder(
                fødselsdato = LocalDate.now()
                    .minusYears(18)
            )
        ).isTrue

        assertThat(
            oppslagServiceService.erIAktuellAlder(
                fødselsdato = LocalDate.now()
                    .minusYears(19).plusDays(1)
            )
        )
            .withFailMessage("Personen har ikke fylt 19 ennå")
            .isTrue
        assertThat(
            oppslagServiceService.erIAktuellAlder(
                fødselsdato = LocalDate.now()
                    .minusYears(19).minusDays(2)
            )
        )
            .isFalse
    }

    @Test
    fun `Skal filtrere bort døde barn`() {

        val mapper = SøkerinfoMapper(kodeverkService = mockk())
        val oppslagServiceServiceMedMapper = OppslagServiceServiceImpl(
            pdlClient,
            pdlStsClient, regelverkConfig, mapper
        )

        every { pdlStsClient.hentBarn(any()) } returns mapOf(pdlBarn(dødsfall = Dødsfall(LocalDate.MIN)))
        mockHentPersonPdlClient()
        val søkerinfo = oppslagServiceServiceMedMapper.hentSøkerinfo()

        assertThat(søkerinfo.barn).hasSize(0)
    }

    @Test
    fun `Skal ikke filtrere bort levende barn`() {
        val mapper = SøkerinfoMapper(kodeverkService = mockk())
        val oppslagServiceServiceMedMapper = OppslagServiceServiceImpl(
            pdlClient,
            pdlStsClient, regelverkConfig, mapper
        )
        every { pdlStsClient.hentBarn(any()) } returns mapOf(pdlBarn())
        mockHentPersonPdlClient()
        val søkerinfo = oppslagServiceServiceMedMapper.hentSøkerinfo()
        assertThat(søkerinfo.barn).hasSize(1)
    }

    @Test
    fun `Skal filtrere bort døde barn og barn i for høy alder`() {
        val dødtBarn = pdlBarn(null, Dødsfall(LocalDate.MIN), LocalDate.now().minusYears(1))
        val levendeBarn = pdlBarn(fødselsdato = LocalDate.now().minusYears(2))
        val barnForHøyAlder = pdlBarn(fødselsdato = LocalDate.now().minusYears(20))

        every { pdlStsClient.hentBarn(any()) } returns mapOf(dødtBarn, levendeBarn, barnForHøyAlder)

        mockHentPersonPdlClient()

        val søkerinfo = oppslagServiceService.hentSøkerinfo()
        assertThat(søkerinfo.barn).hasSize(1)
        assertThat(søkerinfo.barn.first().fødselsdato).isEqualTo(levendeBarn.second.fødsel.first().fødselsdato)
    }

    private fun captureAktuelleBarn(aktuelleBarnSlot: CapturingSlot<Map<String, PdlBarn>>) {
        every {
            søkerinfoMapper.mapTilSøkerinfo(any(), capture(aktuelleBarnSlot), mutableMapOf())
        } returns mockk()
    }

    private fun pdlBarn(
        adressebeskyttelse: Adressebeskyttelse? = null,
        dødsfall: Dødsfall? = null,
        fødselsdato: LocalDate = LocalDate.now().minusMonths(6),
        familierelasjoner: List<Familierelasjon> = listOf()
    ): Pair<String, PdlBarn> {
        val fødsel = Fødsel(fødselsdato.year, fødselsdato)
        return Pair(fødselsdato.format(ISO_LOCAL_DATE),
                    PdlBarn(adressebeskyttelse = adressebeskyttelse?.let { listOf(adressebeskyttelse) } ?: emptyList(),
                            bostedsadresse = emptyList(),
                            deltBosted = emptyList(),
                            fødsel = listOf(fødsel),
                            navn = emptyList(),
                            dødsfall = dødsfall?.let { listOf(dødsfall) } ?: emptyList(),
                            familierelasjoner = familierelasjoner))
    }


    private fun mockPdlHentBarn(
        navn: String = "Ola",
        adressebeskyttelseGradering: AdressebeskyttelseGradering = UGRADERT
    ) {
        val pdlBarn = pdlBarn(Adressebeskyttelse(adressebeskyttelseGradering))
        val copy = pdlBarn.second.copy(navn = listOf(Navn(navn, navn, navn)))
        every { pdlStsClient.hentBarn(any()) } returns (mapOf(pdlBarn.first to copy))
    }


    private fun mockHentPersonPdlClient(
        fornavn: String = "TestNavn",
        mellomnavn: String = "TestNavn",
        etternavn: String = "TestNavn",
        adressebeskyttelseGradering: AdressebeskyttelseGradering = UGRADERT
    ) {

        every { pdlClient.hentSøker(any()) } returns (PdlSøker(
            listOf(Adressebeskyttelse(adressebeskyttelseGradering)),
            listOf(),
            listOf(),
            navn = listOf(Navn(fornavn, mellomnavn, etternavn)),
            sivilstand = listOf(Sivilstand(Sivilstandstype.UOPPGITT)),
            listOf()
        ))
    }


    fun mapTilPerson(personinfoDto: PersoninfoDto): Person {
        return Person(
            personinfoDto.ident,
            personinfoDto.navn.forkortetNavn,
            mapTilAdresse(personinfoDto.adresseinfo),
            personinfoDto.egenansatt?.isErEgenansatt ?: false,
            personinfoDto.sivilstand?.kode?.verdi ?: "",
            søkerinfoMapper.hentLand(personinfoDto.statsborgerskap?.kode?.verdi),
            false
        )
    }

    private fun mapTilAdresse(adresseinfoDto: AdresseinfoDto?): Adresse {
        val postnummer: String? = adresseinfoDto?.bostedsadresse?.postnummer
        return Adresse(
            adresse = adresseinfoDto?.bostedsadresse?.adresse
                ?: "",
            postnummer = postnummer ?: "",
            poststed = søkerinfoMapper.hentPoststed(postnummer)
        )
    }

}
