package no.nav.familie.ef.søknad.integration.pdl

import com.fasterxml.jackson.databind.SerializationFeature
import no.nav.familie.kontrakter.felles.objectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PdlDtoTest {

    @Test
    fun `pdlSøkerData inneholder samme felter som blir spurt om i query`() {
        val spørringsfelter = PdlTestUtil.parseSpørring("/pdl/søker.graphql")

        val dtoFelter = PdlTestUtil.finnFeltStruktur(PdlTestdata.pdlSøkerData)!!

        assertThat(dtoFelter).isEqualTo(spørringsfelter["data"])
    }

    @Test
    fun `pdlBarnData inneholder samme felter som blir spurt om i query`() {
        val spørringsfelter = PdlTestUtil.parseSpørring("/pdl/barn.graphql")

        val dtoFelter = PdlTestUtil.finnFeltStruktur(PdlTestdata.pdlBarnData)!!

        val writerWithDefaultPrettyPrinter = objectMapper
            .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
            .writerWithDefaultPrettyPrinter()
        assertThat(writerWithDefaultPrettyPrinter.writeValueAsString(dtoFelter))
            .isEqualTo(writerWithDefaultPrettyPrinter.writeValueAsString(spørringsfelter["data"]))
    }
}
