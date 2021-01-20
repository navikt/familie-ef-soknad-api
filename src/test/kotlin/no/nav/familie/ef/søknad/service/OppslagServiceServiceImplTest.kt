package no.nav.familie.ef.søknad.service

import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.spyk
import no.nav.familie.ef.søknad.config.RegelverkConfig
import no.nav.familie.ef.søknad.integration.PdlClient
import no.nav.familie.ef.søknad.integration.PdlStsClient
import no.nav.familie.ef.søknad.integration.TpsInnsynServiceClient
import no.nav.familie.ef.søknad.integration.dto.NavnDto
import no.nav.familie.ef.søknad.integration.dto.PersoninfoDto
import no.nav.familie.ef.søknad.integration.dto.RelasjonDto
import no.nav.familie.ef.søknad.integration.dto.pdl.Adressebeskyttelse
import no.nav.familie.ef.søknad.integration.dto.pdl.AdressebeskyttelseGradering
import no.nav.familie.ef.søknad.integration.dto.pdl.Dødsfall
import no.nav.familie.ef.søknad.integration.dto.pdl.Fødsel
import no.nav.familie.ef.søknad.integration.dto.pdl.Navn
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlBarn
import no.nav.familie.ef.søknad.integration.dto.pdl.PdlSøker
import no.nav.familie.ef.søknad.integration.dto.pdl.Sivilstand
import no.nav.familie.ef.søknad.integration.dto.pdl.Sivilstandstype
import no.nav.familie.ef.søknad.mapper.SøkerinfoMapper
import no.nav.familie.ef.søknad.mock.TpsInnsynMockController
import no.nav.familie.kontrakter.felles.objectMapper
import no.nav.familie.sikkerhet.EksternBrukerUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class OppslagServiceServiceImplTest {

    val tpsClient: TpsInnsynServiceClient = mockk()
    val pdlClient: PdlClient = mockk()
    val tpsInnsynMockController = TpsInnsynMockController()
    val regelverkConfig: RegelverkConfig = RegelverkConfig(RegelverkConfig.Alder(18))
    val pdlStsClient: PdlStsClient = mockk()

    private val søkerinfoMapper = spyk(SøkerinfoMapper(mockk(relaxed = true)))
    private val oppslagServiceService = OppslagServiceServiceImpl(tpsClient, pdlClient,
                                                                  pdlStsClient, regelverkConfig, søkerinfoMapper)

    @BeforeEach
    fun setUp() {
        mockkObject(EksternBrukerUtils)
        every { EksternBrukerUtils.hentFnrFraToken() } returns "12345678911"
        mockHentBarn()
        mockPdlHentBarn()
        mockHentPersonInfo()
        mockHentPersonPdlClient()
    }

    @Test
    fun `Lik søkerInfo skal ha lik hash`() {
        mockPdlHentBarn()
        val søkerinfo = oppslagServiceService.hentSøkerinfo()
        mockHentPersonPdlClient()

        val søkerinfo2 = oppslagServiceService.hentSøkerinfo()
        assertEquals(søkerinfo.hash, søkerinfo2.hash)
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
        mockHentBarn("AnnetBarnenavn")
        mockPdlHentBarn("AnnetBarnenavn")
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
        assertThat(oppslagServiceService.erIAktuellAlder(fødselsdato = LocalDate.now()
                .minusYears(18))).isTrue

        assertThat(oppslagServiceService.erIAktuellAlder(fødselsdato = LocalDate.now()
                .minusYears(19).plusDays(1)))
                .withFailMessage("Personen har ikke fylt 19 ennå")
                .isTrue
        assertThat(oppslagServiceService.erIAktuellAlder(fødselsdato = LocalDate.now()
                .minusYears(19).minusDays(2)))
                .isFalse
    }

    @Test
    fun `Skal filtrere bort døde barn`() {
        every { pdlStsClient.hentBarn(any()) } returns mapOf(pdlBarn(dødsfall = Dødsfall(LocalDate.MIN)))
        val aktuelleBarnSlot = slot<Map<String, PdlBarn>>()
        mockHentPersonPdlClient()
        captureAktuelleBarn(aktuelleBarnSlot)
        oppslagServiceService.hentSøkerinfo()
        assertThat(aktuelleBarnSlot.captured).hasSize(0)
    }

    @Test
    fun `Skal filtrere bort barn med adressebeskyttelse`() {
        every { pdlStsClient.hentBarn(any()) } returns mapOf(pdlBarn(adressebeskyttelse = Adressebeskyttelse(
                AdressebeskyttelseGradering.STRENGT_FORTROLIG)))
        val aktuelleBarnSlot = slot<Map<String, PdlBarn>>()
        mockHentPersonPdlClient()
        captureAktuelleBarn(aktuelleBarnSlot)
        oppslagServiceService.hentSøkerinfo()
        assertThat(aktuelleBarnSlot.captured).hasSize(0)
    }

    @Test
    fun `Skal ikke filtrere bort levende barn`() {
        every { pdlStsClient.hentBarn(any()) } returns mapOf(pdlBarn())
        val aktuelleBarnSlot = slot<Map<String, PdlBarn>>()
        mockHentPersonPdlClient()
        captureAktuelleBarn(aktuelleBarnSlot)
        oppslagServiceService.hentSøkerinfo()
        assertThat(aktuelleBarnSlot.captured).hasSize(1)
    }

    @Test
    fun `Skal filtrere bort døde barn og barn i for høy alder`() {
        val dødtBarn = pdlBarn(null, Dødsfall(LocalDate.MIN), LocalDate.now().minusYears(1))
        val levendeBarn = pdlBarn(fødselsdato = LocalDate.now().minusYears(2))
        val barnForHøyAlder = pdlBarn(fødselsdato = LocalDate.now().minusYears(20))

        every { pdlStsClient.hentBarn(any()) } returns mapOf(dødtBarn, levendeBarn, barnForHøyAlder)
        val aktuelleBarnSlot = slot<Map<String, PdlBarn>>()
        mockHentPersonPdlClient()
        captureAktuelleBarn(aktuelleBarnSlot)
        oppslagServiceService.hentSøkerinfo()
        assertThat(aktuelleBarnSlot.captured).hasSize(1)
        assertThat(aktuelleBarnSlot.captured).containsKey(levendeBarn.first)
    }

    private fun captureAktuelleBarn(aktuelleBarnSlot: CapturingSlot<Map<String, PdlBarn>>) {
        every {
            søkerinfoMapper.mapTilSøkerinfo(any(), capture(aktuelleBarnSlot))
        } returns mockk()
    }

    private fun pdlBarn(
            adressebeskyttelse: Adressebeskyttelse? = null,
            dødsfall: Dødsfall? = null,
            fødselsdato: LocalDate = LocalDate.now().minusMonths(6),
    ): Pair<String, PdlBarn> {
        val fødsel = Fødsel(fødselsdato.year, fødselsdato)
        return Pair(fødselsdato.format(ISO_LOCAL_DATE),
                    PdlBarn(adressebeskyttelse = adressebeskyttelse?.let { listOf(adressebeskyttelse) } ?: emptyList(),
                            bostedsadresse = emptyList(),
                            deltBosted = emptyList(),
                            fødsel = listOf(fødsel),
                            navn = emptyList(),
                            dødsfall = dødsfall?.let { listOf(dødsfall) } ?: emptyList()))
    }

    private fun mockHentBarn(navn: String = "Ola") {
        val barnFraTpsMocked = tpsInnsynMockController.barnFraTpsMocked()
        val collectionType =
                objectMapper.typeFactory.constructCollectionType(List::class.java, RelasjonDto::class.java)
        val barnListDto: List<RelasjonDto> = objectMapper.readValue(barnFraTpsMocked, collectionType)
        val copyAvBarn = barnListDto[0].copy(forkortetNavn = navn)
        every { tpsClient.hentBarn() } returns (listOf(copyAvBarn))
    }

    private fun mockPdlHentBarn(navn: String = "Ola") {
        val pdlBarn = pdlBarn()
        val copy = pdlBarn.second.copy(navn = listOf(Navn(navn, navn, navn)))
        every { pdlStsClient.hentBarn(any()) } returns (mapOf(pdlBarn.first to copy))
    }

    private fun mockHentPersonInfo(nyttNavn: String = "TestNavn") {
        val søkerinfoFraTpsMocked = tpsInnsynMockController.søkerinfoFraTpsMocked()
        val personInfoDto: PersoninfoDto = objectMapper.readValue(søkerinfoFraTpsMocked, PersoninfoDto::class.java)
        every { tpsClient.hentPersoninfo() } returns (personInfoDto.copy(navn = NavnDto(nyttNavn)))
    }

    private fun mockHentPersonPdlClient(fornavn: String = "TestNavn",
                                        mellomnavn: String = "TestNavn",
                                        etternavn: String = "TestNavn") {

        every { pdlClient.hentSøker(any()) } returns (PdlSøker(listOf(),
                                                               listOf(),
                                                               listOf(),
                                                               navn = listOf(Navn(fornavn, mellomnavn, etternavn)),
                                                               sivilstand = listOf(Sivilstand(Sivilstandstype.UOPPGITT)),
                                                               listOf()))
    }

}
