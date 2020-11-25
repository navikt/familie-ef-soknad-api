package no.nav.familie.ef.søknad.service

import io.mockk.*
import no.nav.familie.ef.søknad.config.RegelverkConfig
import no.nav.familie.ef.søknad.integration.PdlClient
import no.nav.familie.ef.søknad.integration.PdlStsClient
import no.nav.familie.ef.søknad.integration.TpsInnsynServiceClient
import no.nav.familie.ef.søknad.integration.dto.NavnDto
import no.nav.familie.ef.søknad.integration.dto.PersoninfoDto
import no.nav.familie.ef.søknad.integration.dto.RelasjonDto
import no.nav.familie.ef.søknad.integration.dto.pdl.*
import no.nav.familie.ef.søknad.mapper.SøkerinfoMapper
import no.nav.familie.ef.søknad.mock.TpsInnsynMockController
import no.nav.familie.kontrakter.felles.objectMapper
import no.nav.familie.sikkerhet.EksternBrukerUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
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
    private val oppslagServiceService = OppslagServiceServiceImpl(tpsClient, mockk(), pdlClient,
                                                                  pdlStsClient, regelverkConfig, søkerinfoMapper)

    @Before
    fun setUp() {
        mockkObject(EksternBrukerUtils)
        every { EksternBrukerUtils.hentFnrFraToken() } returns "12345678911"
        mockHentBarn()
        mockHentPersonInfo()
        mockHentPersonPdlClient()
    }


    @Test
    fun `Skal ikke logge forskjeller når alt er likt`() {
        val søkerinfo = objectMapper.readValue(søkerInfo, Søkerinfo::class.java)
        val søkerinfo2 = søkerinfo.copy()
        assertThat(oppslagServiceService.hentDiff(søkerinfo, søkerinfo2)).isBlank()
    }

    @Test
    fun `Skal logge forskjeller på Person og barn data`() {
        val søkerinfo = objectMapper.readValue(søkerInfo, Søkerinfo::class.java)
        val endretSøker = søkerinfo.søker.copy(fnr = "54325432",
                                               adresse = Adresse("nyAdresse", "7654", "AnnetSted"),
                                               statsborgerskap = "ANNET",
                                               sivilstand = "UKJENT",
                                               egenansatt = true,
                                               forkortetNavn = "KORT")
        val barn = Barn("456776544567",
                        "Ole Annetnavn",
                        2,
                        LocalDate.now(),
                        false)
        val søkerinfo2 = søkerinfo.copy(søker = endretSøker, barn = listOf(barn))
        assertThat(oppslagServiceService.hentDiff(søkerinfo, søkerinfo2)).isNotBlank
    }

    @Test
    fun `Skal logge forskjeller - manglende barn`() {
        val søkerinfo = objectMapper.readValue(søkerInfo, Søkerinfo::class.java)
        val søkerinfo2 = søkerinfo.copy(barn = listOf())
        assertThat(oppslagServiceService.hentDiff(søkerinfo, søkerinfo2)).isNotBlank()
    }


    @Test
    fun `Lik søkerInfo skal ha lik hash`() {
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
        val søkerinfo = oppslagServiceService.hentSøkerinfo()
        mockHentBarn("AnnetBarnenavn")
        val søkerinfo2 = oppslagServiceService.hentSøkerinfo()
        assertNotEquals(søkerinfo.hash, søkerinfo2.hash)
    }

    @Test
    fun `Test filtrering på dødsdato`() {
        assertThat(oppslagServiceService.erILive(pdlBarn(Dødsfall(LocalDate.MIN)).second)).isFalse
        assertThat(oppslagServiceService.erILive(pdlBarn().second)).isTrue
    }

    @Test
    fun `erIAktuellAlder`() {
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
        every { pdlStsClient.hentBarn(any()) } returns mapOf(pdlBarn(Dødsfall(LocalDate.MIN)))
        val aktuelleBarnSlot = slot<Map<String, PdlBarn>>()
        mockHentPersonPdlClient()
        captureAktuelleBarn(aktuelleBarnSlot)
        oppslagServiceService.hentSøkerinfoV2()
        assertThat(aktuelleBarnSlot.captured).hasSize(0)
    }

    @Test
    fun `Skal ikke filtrere bort levende barn`() {
        every { pdlStsClient.hentBarn(any()) } returns mapOf(pdlBarn())
        val aktuelleBarnSlot = slot<Map<String, PdlBarn>>()
        mockHentPersonPdlClient()
        captureAktuelleBarn(aktuelleBarnSlot)
        oppslagServiceService.hentSøkerinfoV2()
        assertThat(aktuelleBarnSlot.captured).hasSize(1)
    }

    @Test
    fun `Skal filtrere bort døde barn og barn i for høy alder`() {
        val dødtBarn = pdlBarn(Dødsfall(LocalDate.MIN), LocalDate.now().minusYears(1))
        val levendeBarn = pdlBarn(fødselsdato = LocalDate.now().minusYears(2))
        val barnForHøyAlder = pdlBarn(fødselsdato = LocalDate.now().minusYears(20))

        every { pdlStsClient.hentBarn(any()) } returns mapOf(dødtBarn, levendeBarn, barnForHøyAlder)
        val aktuelleBarnSlot = slot<Map<String, PdlBarn>>()
        mockHentPersonPdlClient()
        captureAktuelleBarn(aktuelleBarnSlot)
        oppslagServiceService.hentSøkerinfoV2()
        assertThat(aktuelleBarnSlot.captured).hasSize(1)
        assertThat(aktuelleBarnSlot.captured).containsKey(levendeBarn.first)
    }

    private fun captureAktuelleBarn(aktuelleBarnSlot: CapturingSlot<Map<String, PdlBarn>>) {
        every {
            søkerinfoMapper.mapTilSøkerinfo(any(), capture(aktuelleBarnSlot))
        } returns mockk()
    }

    private fun pdlBarn(dødsfall: Dødsfall? = null,
                        fødselsdato: LocalDate = LocalDate.now().minusMonths(6)): Pair<String, PdlBarn> {
        val fødsel = Fødsel(fødselsdato.year, fødselsdato)
        return Pair(fødselsdato.format(ISO_LOCAL_DATE),
                    PdlBarn(emptyList(),
                            emptyList(),
                            emptyList(),
                            fødsel = listOf(fødsel),
                            dødsfall = dødsfall?.let { listOf(dødsfall) } ?: emptyList()))
    }

    private fun mockHentBarn(navn: String = "Ola") {
        val barnFraTpsMocked = tpsInnsynMockController.barnFraTpsMocked()
        val collectionType =
                objectMapper.getTypeFactory().constructCollectionType(List::class.java, RelasjonDto::class.java)
        val barnListDto: List<RelasjonDto> = objectMapper.readValue(barnFraTpsMocked, collectionType)
        val copyAvBarn = barnListDto[0].copy(forkortetNavn = navn)
        every { tpsClient.hentBarn() } returns (listOf(copyAvBarn))
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
                                                               navn = listOf(Navn(fornavn, mellomnavn, etternavn)),
                                                               listOf(),
                                                               listOf()))
    }

}
