package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.mapper.kontrakt.PersonaliaMapper
import no.nav.familie.ef.søknad.mock.søknadDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


internal class PersonaliaMapperTest {

    @Test
    fun `mapPersonalia mapper dto fra frontend til forventet Personalia`() {
        // Given
        val søknadDto = søknadDto()
        // When
        val personaliaFraSøknadDto = PersonaliaMapper.map(søknadDto.person.søker).verdi
        // Then
        assertThat(personaliaFraSøknadDto.toString()).isEqualTo(personalia().toString())
    }
}
