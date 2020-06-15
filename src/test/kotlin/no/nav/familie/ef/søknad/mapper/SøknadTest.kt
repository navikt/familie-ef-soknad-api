package no.nav.familie.ef.søknad.mapper

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.ef.søknad.mapper.kontrakt.SøknadMapper
import no.nav.familie.ef.søknad.service.DokumentService
import no.nav.familie.kontrakter.felles.objectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDateTime

internal class JsonSisteInnspurtMapperTest {

    private val dokumentServiceServiceMock: DokumentService = mockk()
    private val mapper = SøknadMapper(dokumentServiceServiceMock)
    fun søknad(): SøknadDto = objectMapper.readValue(File("src/test/resources/sisteinnspurt/underUtanningFeil.json"),
                                                     SøknadDto::class.java)
    fun søknadNyttBarn(): SøknadDto = objectMapper.readValue(File("src/test/resources/sisteinnspurt/nyttBarnFeil.json"),
                                                     SøknadDto::class.java)

    private val søknadDto = søknad()


    private val innsendingMottatt: LocalDateTime = LocalDateTime.now()

    @BeforeEach
    fun setUp() {
        every { dokumentServiceServiceMock.hentVedlegg(any()) } returns "DOKUMENTID123".toByteArray()
    }

    @Test
    fun `mapTilIntern returnerer dto med riktig sivilstatus fra frontend`() {

        mapper.mapTilIntern(søknadDto, innsendingMottatt)

    }

    @Test
    internal fun `skal mappe nytt barn uten fødselsnummer`() {
        mapper.mapTilIntern(søknadNyttBarn(), innsendingMottatt)
    }
}
