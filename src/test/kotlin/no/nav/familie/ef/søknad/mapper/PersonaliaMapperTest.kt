package no.nav.familie.ef.søknad.mapper

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.mapper.kontrakt.PersonaliaMapper
import no.nav.familie.ef.søknad.mock.søknadDto
import no.nav.familie.ef.søknad.service.DokumentService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class PersonaliaMapperTest {

    private val dokumentServiceServiceMock: DokumentService = mockk()

    @BeforeEach
    fun setUp() {
        every { dokumentServiceServiceMock.hentVedlegg(any()) } returns "DOKUMENTID123".toByteArray()
    }

    @Test
    fun `mapPersonalia mapper dto fra frontend til forventet Personalia`() {
        // Given
        val søknadDto = søknadDto()
        // When
        val personaliaFraSøknadDto = PersonaliaMapper.mapPersonalia(søknadDto.person.søker)
        // Then
        assertThat(personaliaFraSøknadDto.toString()).isEqualTo(personalia().toString())
    }
}
