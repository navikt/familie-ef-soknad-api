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
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class OppslagServiceServiceImplTest {


    val tpsClient: TpsInnsynServiceClient = mockk()
    val pdlClient: PdlClient = mockk()
    val tpsInnsynMockController = TpsInnsynMockController()
    val regelverkConfig: RegelverkConfig = mockk()
    val pdlStsClient: PdlStsClient = mockk()

    private val søkerinfoMapper = spyk(SøkerinfoMapper(mockk(relaxed = true)))
    private val oppslagServiceService = OppslagServiceServiceImpl(tpsClient, pdlClient,
                                                                  pdlStsClient, regelverkConfig, søkerinfoMapper)

    @Before
    fun setUp() {
        mockkObject(EksternBrukerUtils)
        every { EksternBrukerUtils.hentFnrFraToken() } returns "12345678911"
        mockRegelverksConfig()
        mockHentBarn()
        mockHentPersonInfo()
        mockHentPersonPdlClient()
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
        Assertions.assertThat(oppslagServiceService.erILive(pdlBarn(Dødsfall(LocalDate.MIN)))).isFalse
        Assertions.assertThat(oppslagServiceService.erILive(pdlBarn())).isTrue
    }

    @Test
    fun `Skal filtrere bort døde barn`() {
        every { pdlStsClient.hentBarn(any()) } returns mapOf("2" to pdlBarn(Dødsfall(LocalDate.MIN)))
        val aktuelleBarnSlot = slot<Map<String, PdlBarn>>()
        mockHentPersonPdlClient()
        captureAktuelleBarn(aktuelleBarnSlot)
        oppslagServiceService.hentSøkerinfoV2()
        Assertions.assertThat(aktuelleBarnSlot.captured).hasSize(0)
    }

    @Test
    fun `Skal ikke filtrere bort levende barn`() {
        every { pdlStsClient.hentBarn(any()) } returns mapOf("2" to pdlBarn())
        val aktuelleBarnSlot = slot<Map<String, PdlBarn>>()
        mockHentPersonPdlClient()
        captureAktuelleBarn(aktuelleBarnSlot)
        oppslagServiceService.hentSøkerinfoV2()
        Assertions.assertThat(aktuelleBarnSlot.captured).hasSize(1)
    }

    private fun captureAktuelleBarn(aktuelleBarnSlot: CapturingSlot<Map<String, PdlBarn>>) {
        every {
            søkerinfoMapper.mapTilSøkerinfo(any(), capture(aktuelleBarnSlot))
        } returns mockk()
    }

    private fun pdlBarn(dødsfall: Dødsfall? = null): PdlBarn {
        val fødselsdato = LocalDate.now().minusMonths(6)
        val fødsel = Fødsel(fødselsdato.year, fødselsdato, null, null, null)
        return PdlBarn(emptyList(),
                       emptyList(),
                       emptyList(),
                       emptyList(),
                       emptyList(),
                       fødsel = listOf(fødsel),
                       dødsfall = dødsfall?.let { listOf(dødsfall) } ?: emptyList())
    }

    private fun mockRegelverksConfig() {
        every { regelverkConfig.alder } returns RegelverkConfig.Alder(18)
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
                                                               listOf(),
                                                               listOf(),
                                                               navn = listOf(Navn(fornavn, mellomnavn, etternavn)),
                                                               listOf(),
                                                               listOf(),
                                                               listOf()))
    }

}
