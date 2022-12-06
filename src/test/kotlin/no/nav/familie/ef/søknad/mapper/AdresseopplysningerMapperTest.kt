package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.BooleanFelt
import no.nav.familie.ef.søknad.mapper.kontrakt.AdresseopplysningerData
import no.nav.familie.ef.søknad.mapper.kontrakt.AdresseopplysningerMapper
import no.nav.familie.ef.søknad.mock.søknadDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class AdresseopplysningerMapperTest {

    private val søknadDto = søknadDto()
    private val adresseopplysningerData =
        AdresseopplysningerData(søknadDto.søkerBorPåRegistrertAdresse, søknadDto.adresseopplysninger)
    private val dokumenter = mapOf<String, DokumentasjonWrapper>()

    @Test
    fun `mapper verdier for adresse`() {
        val dto = AdresseopplysningerMapper.map(adresseopplysningerData, dokumenter)
        val bosituasjonMapped = dto.verdi

        assertThat(dto.label).isEqualTo("Opplysninger om adresse")
        assertThat(bosituasjonMapped.søkerBorPåRegistrertAdresse?.label).isEqualTo("Bor du på denne adressen?")
        assertThat(bosituasjonMapped.søkerBorPåRegistrertAdresse?.verdi).isEqualTo(false)

        assertThat(bosituasjonMapped.harMeldtAdresseendring?.label).isEqualTo("Har du meldt adresseendring til folkeregisteret?")
        assertThat(bosituasjonMapped.harMeldtAdresseendring?.verdi).isEqualTo(true)
    }

    @Test
    fun `mapper verdier for adresse - personen bor på registrert adresse`() {
        val data = adresseopplysningerData.copy(
            søkerBorPåRegistrertAdresse = BooleanFelt("Spørsmål 1", true),
            adresseopplysninger = null
        )
        val bosituasjonMapped = AdresseopplysningerMapper.map(data, dokumenter).verdi

        assertThat(bosituasjonMapped.søkerBorPåRegistrertAdresse?.label).isEqualTo("Spørsmål 1")
        assertThat(bosituasjonMapped.søkerBorPåRegistrertAdresse?.verdi).isEqualTo(true)

        assertThat(bosituasjonMapped.harMeldtAdresseendring).isNull()
    }

}
