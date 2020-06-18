package no.nav.familie.ef.søknad.mapper

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.ef.søknad.mapper.kontrakt.SøknadMapper
import no.nav.familie.ef.søknad.service.DokumentService
import no.nav.familie.kontrakter.felles.objectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.time.LocalDateTime
import kotlin.test.assertNotNull

internal class JsonSisteInnspurtMapperTest {

    private val dokumentServiceServiceMock: DokumentService = mockk()
    private val mapper = SøknadMapper(dokumentServiceServiceMock)

    private val innsendingMottatt: LocalDateTime = LocalDateTime.now()

    @BeforeEach
    fun setUp() {
        every { dokumentServiceServiceMock.hentVedlegg(any()) } returns "DOKUMENTID123".toByteArray()
    }

    @Test
    fun `Annen forelder skal ha fødselsnummer etter mapping`() {
        fun identTest3(): SøknadDto = objectMapper.readValue(File("src/test/resources/sisteinnspurt/identTest3.json"),
                                                           SøknadDto::class.java)
        val mapped = mapper.mapTilIntern(identTest3(), innsendingMottatt)
        assertNotNull(mapped.søknad.barn.verdi.last().annenForelder?.verdi?.person?.verdi?.fødselsnummer)

    }

    @Test
    fun `skal ikke ha feil i testFntIdent`() {
        fun testfeil(): SøknadDto = objectMapper.readValue(File("src/test/resources/sisteinnspurt/testFntIdent.json"),
                                                           SøknadDto::class.java)
        mapper.mapTilIntern(testfeil(), innsendingMottatt)
    }

    @Test
    fun `skal ikke ha testfeil`() {
        fun testfeil(): SøknadDto = objectMapper.readValue(File("src/test/resources/sisteinnspurt/testfeil.json"),
                                                           SøknadDto::class.java)
        mapper.mapTilIntern(testfeil(), innsendingMottatt)
    }

    @Test
    fun `skal tåle ident`() {
        fun fnrTilIdent(): SøknadDto = objectMapper.readValue(File("src/test/resources/sisteinnspurt/fnrTilIdent.json"),
                                                              SøknadDto::class.java)
        mapper.mapTilIntern(fnrTilIdent(), innsendingMottatt)
    }



    @Test
    fun `underUtanningFeil -mapTilIntern returnerer dto med riktig sivilstatus fra frontend`() {
        fun søknad(): SøknadDto = objectMapper.readValue(File("src/test/resources/sisteinnspurt/underUtanningFeil.json"),
                                                         SøknadDto::class.java)
        mapper.mapTilIntern(søknad(), innsendingMottatt)
    }

    @Test
    fun `søknadFraEivind mapTilIntern - mapper String med desimaler til heltall`() {
        fun søknadFraEivind(): SøknadDto = objectMapper.readValue(File("src/test/resources/sisteinnspurt/feilfraEivind.json"),
                                                                  SøknadDto::class.java)
        mapper.mapTilIntern(søknadFraEivind(), innsendingMottatt)
    }

    @Test
    internal fun `nyttBarnFeil - skal mappe nytt barn uten fødselsnummer`() {
        fun søknadNyttBarn(): SøknadDto = objectMapper.readValue(File("src/test/resources/sisteinnspurt/nyttBarnFeil.json"), SøknadDto::class.java)
        mapper.mapTilIntern(søknadNyttBarn(), innsendingMottatt)
    }

    @Test
    internal fun `søknadMedUgyldigFødselsnummer skal kaste exception når fødselsnummer ikke er gyldig`() {
        fun søknadMedugyldigFødselsnummer(): SøknadDto = objectMapper.readValue(File("src/test/resources/sisteinnspurt/søknadMedUgyldigFødselsnummer.json"),
                                                                                SøknadDto::class.java)
        val frontendDto = søknadMedugyldigFødselsnummer()
        assertThrows<IllegalStateException> { mapper.mapTilIntern(frontendDto, innsendingMottatt)}
    }
}
