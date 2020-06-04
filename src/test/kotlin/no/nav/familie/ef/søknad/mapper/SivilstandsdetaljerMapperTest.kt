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
        val kontrakt = SivilstandsdetaljerMapper.mapSivilstandsdetaljer(søknadDto, dokumentMap())
        // Then
        val sivilstatus = søknadDto.sivilstatus

        assertThat(kontrakt.søktOmSkilsmisseSeparasjon?.label).isEqualTo(sivilstatus.harSøktSeparasjon?.label)
        assertThat(kontrakt.søktOmSkilsmisseSeparasjon?.verdi).isEqualTo(sivilstatus.harSøktSeparasjon?.verdi)

        assertThat(kontrakt.datoSøktSeparasjon?.label).isEqualTo(sivilstatus.datoSøktSeparasjon?.label)
        assertThat(kontrakt.datoSøktSeparasjon?.verdi).isEqualTo(sivilstatus.datoSøktSeparasjon?.verdi)

        assertThat(kontrakt.erUformeltGift?.label).isEqualTo(sivilstatus.erUformeltGift.label)
        assertThat(kontrakt.erUformeltGift?.verdi).isEqualTo(sivilstatus.erUformeltGift.verdi)

        assertThat(kontrakt.årsakEnslig?.label).isEqualTo(sivilstatus.årsakEnslig?.label)
        assertThat(kontrakt.årsakEnslig?.verdi).isEqualTo(sivilstatus.årsakEnslig?.verdi)

        assertThat(kontrakt.fraflytningsdato?.label).isEqualTo(sivilstatus.datoFlyttetFraHverandre?.label)
        assertThat(kontrakt.fraflytningsdato?.verdi).isEqualTo(sivilstatus.datoFlyttetFraHverandre?.verdi)

        assertThat(kontrakt.endringSamværsordningDato?.label).isEqualTo(sivilstatus.datoEndretSamvær?.label)
        assertThat(kontrakt.endringSamværsordningDato?.verdi).isEqualTo(sivilstatus.datoEndretSamvær?.verdi)


        assertThat(kontrakt.erUformeltSeparertEllerSkilt?.label).isEqualTo(sivilstatus.erUformeltSeparertEllerSkilt.label)
        assertThat(kontrakt.erUformeltSeparertEllerSkilt?.verdi).isEqualTo(sivilstatus.erUformeltSeparertEllerSkilt.verdi)

        assertThat(kontrakt.samlivsbruddsdato?.label).isEqualTo(sivilstatus.datoForSamlivsbrudd?.label)
        assertThat(kontrakt.samlivsbruddsdato?.verdi).isEqualTo(sivilstatus.datoForSamlivsbrudd?.verdi)
    }
}
