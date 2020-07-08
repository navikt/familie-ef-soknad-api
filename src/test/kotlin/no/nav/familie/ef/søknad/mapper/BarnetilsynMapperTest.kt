package no.nav.familie.ef.søknad.mapper

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.BarnetilsynDto
import no.nav.familie.ef.søknad.mapper.kontrakt.BarnetilsynSøknadMapper
import no.nav.familie.ef.søknad.service.DokumentService
import no.nav.familie.kontrakter.felles.objectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDateTime
import kotlin.test.assertNotNull

internal class BarnetilsynMapperTest {

    private val dokumentServiceServiceMock: DokumentService = mockk()
    private val mapper = BarnetilsynSøknadMapper(dokumentServiceServiceMock)

    private val innsendingMottatt: LocalDateTime = LocalDateTime.now()

    @BeforeEach
    fun setUp() {
        every { dokumentServiceServiceMock.hentVedlegg(any()) } returns "DOKUMENTID123".toByteArray()
    }

    @Test
    fun `Barnetilsyn skal mappes `() {
        val søknad = objectMapper.readValue(File("src/test/resources/barnetilsyn/Barnetilsynsøknad.json"),
                                            BarnetilsynDto::class.java)
        val mapped = mapper.mapTilIntern(søknad, innsendingMottatt)

        assertNotNull(mapped.barnetilsyn.søknad.barn.verdi.first().navn?.verdi)
    }


}

