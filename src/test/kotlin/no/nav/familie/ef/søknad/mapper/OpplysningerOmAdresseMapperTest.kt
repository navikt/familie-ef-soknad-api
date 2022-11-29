package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.mapper.kontrakt.OpplysningerOmAdresseData
import no.nav.familie.ef.søknad.mapper.kontrakt.OpplysningerOmAdresseMapper
import no.nav.familie.ef.søknad.mock.søknadDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class OpplysningerOmAdresseMapperTest {

    private val søknadDto = søknadDto()
    private val opplysningerOmAdresseData =
        OpplysningerOmAdresseData(søknadDto.søkerBorPåRegistrertAdresse, søknadDto.opplysningerOmAdresse)
    private val dokumenter = mapOf<String, DokumentasjonWrapper>()

    @Test
    fun `mapper verdier for adresse`() {
        val bosituasjonMapped = OpplysningerOmAdresseMapper.map(opplysningerOmAdresseData, dokumenter).verdi

        assertThat(bosituasjonMapped.søkerBorPåRegistrertAdresse?.label).isEqualTo("Bor du på denne adressen?")
        assertThat(bosituasjonMapped.søkerBorPåRegistrertAdresse?.verdi).isEqualTo(false)

        assertThat(bosituasjonMapped.harMeldtFlytteendring?.label).isEqualTo("Har du meldt adresseendring til folkeregisteret?")
        assertThat(bosituasjonMapped.harMeldtFlytteendring?.verdi).isEqualTo(true)
    }

}
