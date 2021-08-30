package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadBarnetilsynDto
import no.nav.familie.ef.søknad.mapper.kontrakt.SøknadBarnetilsynMapper
import no.nav.familie.kontrakter.felles.objectMapper
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDateTime
import kotlin.test.assertNotNull

internal class SøknadBarnetilsynMapperTest {

    private val mapper = SøknadBarnetilsynMapper()

    private val innsendingMottatt: LocalDateTime = LocalDateTime.now()

    @Test
    fun `Barnetilsyn skal mappes `() {
        val søknad = objectMapper.readValue(File("src/test/resources/barnetilsyn/Barnetilsynsøknad.json"),
                                            SøknadBarnetilsynDto::class.java)
        val mapped = mapper.mapTilIntern(søknad, innsendingMottatt)

        assertNotNull(mapped.søknad.barn.verdi.first().navn?.verdi)
    }

}
