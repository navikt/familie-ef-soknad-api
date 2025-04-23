package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.mock.søknadOvergangsstønadDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PersonaliaMapperTest {
    @Test
    fun `mapPersonalia mapper dto fra frontend til forventet Personalia`() {
        // Given
        val søknadDto = søknadOvergangsstønadDto()
        // When
        val personaliaFraSøknadDto = PersonaliaMapper.map(søknadDto.person.søker).verdi
        // Then
        assertThat(personaliaFraSøknadDto.toString()).isEqualTo(personalia().toString())
    }
}
