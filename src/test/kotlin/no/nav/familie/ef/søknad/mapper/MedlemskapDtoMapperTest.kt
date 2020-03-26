package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.BooleanFelt
import no.nav.familie.ef.søknad.mapper.kontrakt.MedlemsskapsMapper
import no.nav.familie.ef.søknad.mock.søknadDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MedlemskapDtoMapperTest {

    private val søknadDto = søknadDto()


    @Test
    fun `mapPersonalia mapper dto mapper bosatt til false`() {
        // When
        val medlemsskapDetaljer = MedlemsskapsMapper.mapMedlemskap(søknadDto, mapOf())
        // Then
        assertThat(medlemsskapDetaljer.bosattNorgeSisteÅrene.verdi).isEqualTo(false)
    }

    @Test
    fun `mapPersonalia mapper dto mapper bosatt til true`() {
        // Given
        val label = "Bodd i Norge?"
        val medlemskap = søknadDto.medlemskap.copy(søkerBosattINorgeSisteTreÅr = BooleanFelt(label, true))
        val søknad = søknadDto.copy(medlemskap = medlemskap)
        // When
        val medlemsskapDetaljer = MedlemsskapsMapper.mapMedlemskap(søknad, mapOf())
        // Then
        assertThat(medlemsskapDetaljer.bosattNorgeSisteÅrene.verdi).isEqualTo(true)
        assertThat(medlemsskapDetaljer.bosattNorgeSisteÅrene.label).isEqualTo(label)
    }

    @Test
    fun `mapPersonalia mapper perioder bodd i utland`() {
        // When
        val medlemsskapDetaljer = MedlemsskapsMapper.mapMedlemskap(søknadDto, mapOf())
        // Then
        assertThat(medlemsskapDetaljer.utenlandsopphold?.verdi).hasSize(2)
    }
}