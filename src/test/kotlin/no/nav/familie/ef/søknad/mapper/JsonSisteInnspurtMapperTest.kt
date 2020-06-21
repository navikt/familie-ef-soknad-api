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
import kotlin.test.assertNull

internal class JsonSisteInnspurtMapperTest {

    private val dokumentServiceServiceMock: DokumentService = mockk()
    private val mapper = SøknadMapper(dokumentServiceServiceMock)

    private val innsendingMottatt: LocalDateTime = LocalDateTime.now()

    @BeforeEach
    fun setUp() {
        every { dokumentServiceServiceMock.hentVedlegg(any()) } returns "DOKUMENTID123".toByteArray()
    }

    @Test
    fun `Barn har tom streng i verdi - datofelt `() {
        val mapped: SøknadDto = objectMapper.readValue(File("src/test/resources/sisteinnspurt/barnTomStrengFødselsdato.json"),
                                                       SøknadDto::class.java)
        val mappetTilBarnUtenFødselsTermindato = mapper.mapTilIntern(mapped, innsendingMottatt)
        assertNull(mappetTilBarnUtenFødselsTermindato.søknad.barn.verdi.first().fødselTermindato)
    }


    @Test
    fun `Preprodtest skal ikke feile med hildefeil`() {
        val mapped: SøknadDto = objectMapper.readValue(File("src/test/resources/sisteinnspurt/hildeFeil400.json"),
                                                       SøknadDto::class.java)
        mapper.mapTilIntern(mapped, innsendingMottatt)

    }

    @Test
    fun `Preprodtest skal ikke feile med karifeil`() {
        // fungerer når søkerFraBestemtMåned er fikset
        val mapped: SøknadDto = objectMapper.readValue(File("src/test/resources/sisteinnspurt/kariFeil400.json"),
                                                       SøknadDto::class.java)
        mapper.mapTilIntern(mapped, innsendingMottatt)

    }


    @Test
    fun `Preprodtest skal ikke feile med mirjafeil`() {
        val mapped: SøknadDto = objectMapper.readValue(File("src/test/resources/sisteinnspurt/mirjaFeil.json"),
                                                       SøknadDto::class.java)
        mapper.mapTilIntern(mapped, innsendingMottatt)

    }


    @Test
    fun `Preprodtest skal ikke feile med donorbarn`() {
        val donorbarn: SøknadDto = objectMapper.readValue(File("src/test/resources/sisteinnspurt/donorbarn.json"),
                                                          SøknadDto::class.java)
        mapper.mapTilIntern(donorbarn, innsendingMottatt)

    }

//   Json mangler forelder på barn -> i UI, kan man få til dette hvis man går tilbake til sine barn, legger til et nytt og returnerer til oppsummering uten å oppdatere info om forelder
//    @Test
//    fun `Mapping skal ikke feile med dato på barn`() {
//        val datoBarn: SøknadDto = objectMapper.readValue(File("src/test/resources/sisteinnspurt/barnDato.json"),
//                                                          SøknadDto::class.java)
//        mapper.mapTilIntern(datoBarn, innsendingMottatt)
//
//    }

    @Test
    fun `Preprodtest skal ikke feile`() {
        fun identTest3(): SøknadDto = objectMapper.readValue(File("src/test/resources/sisteinnspurt/feilFraPreprod.json"),
                                                             SøknadDto::class.java)
        mapper.mapTilIntern(identTest3(), innsendingMottatt)

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
        fun søknadNyttBarn(): SøknadDto = objectMapper.readValue(File("src/test/resources/sisteinnspurt/nyttBarnFeil.json"),
                                                                 SøknadDto::class.java)
        mapper.mapTilIntern(søknadNyttBarn(), innsendingMottatt)
    }

    @Test
    internal fun `søknadMedUgyldigFødselsnummer skal kaste exception når fødselsnummer ikke er gyldig`() {
        fun søknadMedugyldigFødselsnummer(): SøknadDto = objectMapper.readValue(File("src/test/resources/sisteinnspurt/søknadMedUgyldigFødselsnummer.json"),
                                                                                SøknadDto::class.java)

        val frontendDto = søknadMedugyldigFødselsnummer()
        assertThrows<IllegalStateException> { mapper.mapTilIntern(frontendDto, innsendingMottatt) }
    }
}
