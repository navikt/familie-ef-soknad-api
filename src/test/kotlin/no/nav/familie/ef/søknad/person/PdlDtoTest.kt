package no.nav.familie.ef.søknad.person

import no.nav.familie.kontrakter.felles.jsonMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import tools.jackson.databind.SerializationFeature
import tools.jackson.databind.json.JsonMapper

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

        val writerWithDefaultPrettyPrinter: JsonMapper = jsonMapper.rebuild().enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS).build()

        assertThat(writerWithDefaultPrettyPrinter.writeValueAsString(dtoFelter))
            .isEqualTo(writerWithDefaultPrettyPrinter.writeValueAsString(spørringsfelter["data"]))
    }
}
