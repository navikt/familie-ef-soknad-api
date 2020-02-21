package no.nav.familie.ef.søknad.mapper

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.mapper.kontrakt.SivilstandsdetaljerMapper
import no.nav.familie.ef.søknad.mock.søknadDto
import no.nav.familie.ef.søknad.service.DokumentService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class SivilstandsdetaljerMapperTest {

    private val dokumentServiceServiceMock: DokumentService = mockk()

    @BeforeEach
    fun setUp() {
        every { dokumentServiceServiceMock.hentVedlegg(any()) } returns "DOKUMENTID123".toByteArray()
    }

    @Test
    fun `mapSivilstandsdetaljer mapper dto fra frontend til forventet Sivilstandsdetaljer`() {
        // Given
        val søknadDto = søknadDto()
        // When
        val sivilstandsdetaljerFraSøknadDto = SivilstandsdetaljerMapper.mapSivilstandsdetaljer(søknadDto, dokumentMap())
        // Then
        assertThat(sivilstandsdetaljerFraSøknadDto.toString()).isEqualTo(sivilstandsdetaljer().toString())
    }
}
