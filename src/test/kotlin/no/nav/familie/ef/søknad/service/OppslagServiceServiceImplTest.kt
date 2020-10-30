package no.nav.familie.ef.søknad.service

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.config.RegelverkConfig
import no.nav.familie.ef.søknad.integration.PdlClient
import no.nav.familie.ef.søknad.integration.TpsInnsynServiceClient
import no.nav.familie.ef.søknad.integration.dto.NavnDto
import no.nav.familie.ef.søknad.integration.dto.PersoninfoDto
import no.nav.familie.ef.søknad.integration.dto.RelasjonDto
import no.nav.familie.ef.søknad.mapper.SøkerinfoMapper
import no.nav.familie.ef.søknad.mock.TpsInnsynMockController
import no.nav.familie.kontrakter.felles.objectMapper
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class OppslagServiceServiceImplTest {


    val tpsClient: TpsInnsynServiceClient = mockk()
    val pdlClient: PdlClient = mockk()
    val tpsInnsynMockController = TpsInnsynMockController()
    val regelverkConfig: RegelverkConfig = mockk()

    private val søkerinfoMapper = SøkerinfoMapper(mockk(relaxed = true))
    private val oppslagServiceService = OppslagServiceServiceImpl(tpsClient, pdlClient, regelverkConfig, søkerinfoMapper)

    @Before
    fun setUp() {
        mockRegelverksConfig()
        mockHentBarn()
        mockHentPersonInfo()
    }

    @Test
    fun `Lik søkerInfo skal ha lik hash`() {
        val søkerinfo = oppslagServiceService.hentSøkerinfo()
        val søkerinfo2 = oppslagServiceService.hentSøkerinfo()
        assertEquals(søkerinfo.hash, søkerinfo2.hash)
    }

    @Test
    fun `SøkerInfo med ulike navn skal ikke ha lik hash`() {
        val søkerinfo = oppslagServiceService.hentSøkerinfo()
        mockHentPersonInfo("Et annet navn")
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

}
