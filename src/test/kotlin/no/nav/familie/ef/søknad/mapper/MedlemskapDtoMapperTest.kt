package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.mock.søknadDto
import no.nav.familie.ef.søknad.søknad.domain.BooleanFelt
import no.nav.familie.ef.søknad.søknad.domain.TekstFelt
import no.nav.familie.ef.søknad.søknad.mapper.MedlemsskapsMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MedlemskapDtoMapperTest {

    private val medlemskap = søknadDto().medlemskap

    @Test
    fun `mapPersonalia mapper dto mapper bosatt til false`() {
        // When
        val medlemsskapDetaljer = MedlemsskapsMapper.map(medlemskap).verdi
        // Then
        assertThat(medlemsskapDetaljer.bosattNorgeSisteÅrene.verdi).isEqualTo(false)
    }

    @Test
    fun `mapPersonalia mapper dto mapper bosatt til true`() {
        // Given
        val label = "Bodd i Norge?"
        val medlemskap = medlemskap.copy(søkerBosattINorgeSisteTreÅr = BooleanFelt(label, true))
        // When
        val medlemsskapDetaljer = MedlemsskapsMapper.map(medlemskap).verdi
        // Then
        assertThat(medlemsskapDetaljer.bosattNorgeSisteÅrene.verdi).isEqualTo(true)
        assertThat(medlemsskapDetaljer.bosattNorgeSisteÅrene.label).isEqualTo(label)
    }

    @Test
    fun `mapPersonalia mapper dto mapper oppholdsland til null`() {
        // When
        val medlemsskapDetaljer = MedlemsskapsMapper.map(medlemskap).verdi
        // Then
        assertThat(medlemsskapDetaljer.oppholdsland).isEqualTo(null)
    }

    @Test
    fun `mapPersonalia mapper dto mapper oppholdsland til riktige verdier`() {
        // Given
        val label = "I hvilket land opphold du og barna dere?"
        val medlemskap = medlemskap.copy(oppholdsland = TekstFelt(label = label, verdi = "Sverige", svarid = "SWE"))
        // When
        val medlemsskapDetaljer = MedlemsskapsMapper.map(medlemskap).verdi
        // Then
        assertThat(medlemsskapDetaljer.oppholdsland?.verdi).isEqualTo("Sverige")
        assertThat(medlemsskapDetaljer.oppholdsland?.svarId).isEqualTo("SWE")
        assertThat(medlemsskapDetaljer.oppholdsland?.label).isEqualTo(label)
    }

    @Test
    fun `mapPersonalia mapper perioder bodd i utland`() {
        // When
        val medlemsskapDetaljer = MedlemsskapsMapper.map(medlemskap).verdi
        // Then
        assertThat(medlemsskapDetaljer.utenlandsopphold?.verdi).hasSize(2)
        assertThat(medlemsskapDetaljer.utenlandsopphold?.verdi?.get(0)?.land?.svarId).isEqualTo("SWE")
    }
}
