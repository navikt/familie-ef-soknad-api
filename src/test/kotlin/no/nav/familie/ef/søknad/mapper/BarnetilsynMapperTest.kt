package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.BarnetilsynDto
import no.nav.familie.ef.søknad.mapper.kontrakt.BarnetilsynSøknadMapper
import no.nav.familie.ef.søknad.mock.DokumentServiceStub
import no.nav.familie.kontrakter.felles.objectMapper
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDateTime
import kotlin.test.assertNotNull

internal class BarnetilsynMapperTest {

    private val mapper = BarnetilsynSøknadMapper(DokumentServiceStub())

    private val innsendingMottatt: LocalDateTime = LocalDateTime.now()

    @Test //TODO må få in barnepass på en av barnen
    fun `Barnetilsyn skal mappes `() {
        val søknad = objectMapper.readValue(File("src/test/resources/barnetilsyn/Barnetilsynsøknad.json"),
                                            BarnetilsynDto::class.java)
        val mapped = mapper.mapTilIntern(søknad, innsendingMottatt)

        assertNotNull(mapped.barnetilsyn.søknad.barn.verdi.first().navn?.verdi)
    }

}
