package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.BooleanFelt
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

    @Test
    fun `mapper verdier for adresse - personen bor på registrert adresse`() {
        val data = opplysningerOmAdresseData.copy(
            søkerBorPåRegistrertAdresse = BooleanFelt("Spørsmål 1", true),
            opplysningerOmAdresse = null
        )
        val bosituasjonMapped = OpplysningerOmAdresseMapper.map(data, dokumenter).verdi

        assertThat(bosituasjonMapped.søkerBorPåRegistrertAdresse?.label).isEqualTo("Spørsmål 1")
        assertThat(bosituasjonMapped.søkerBorPåRegistrertAdresse?.verdi).isEqualTo(true)

        assertThat(bosituasjonMapped.harMeldtFlytteendring).isNull()
    }

}
