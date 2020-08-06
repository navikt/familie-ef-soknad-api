package no.nav.familie.ef.søknad.mapper

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.mapper.kontrakt.SituasjonsMapper
import no.nav.familie.ef.søknad.mock.søknadDto
import no.nav.familie.ef.søknad.service.DokumentService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class SituasjonMapperTest {
    private val dokumentServiceServiceMock: DokumentService = mockk()
    private val søknadDto = søknadDto()
    private val situasjon = SituasjonsMapper.map(søknadDto, mapOf()).verdi

    @BeforeEach
    fun setUp() {
        every { dokumentServiceServiceMock.hentVedlegg(any()) } returns "DOKUMENTID123".toByteArray()
    }

    @Test
    fun `situasjon label gjelderDetteDeg er mappet`() {
        // Then
        Assertions.assertThat(situasjon.gjelderDetteDeg.label).isEqualTo("Gjelder noe av dette deg?")
    }

    @Test
    fun `situasjon verdi for gjelderDetteDeg er mappet`() {
        // Then
        Assertions.assertThat(situasjon.gjelderDetteDeg.verdi).hasSize(14)
    }

    @Test
    fun `situasjon label for oppstartNyJobb er mappet`() {
        // Then
        Assertions.assertThat(situasjon.oppstartNyJobb?.label).isEqualTo("Når skal du starte i ny jobb?")
    }

    @Test
    fun `situasjon verdi for oppstartNyJobb er mappet`() {
        // Then
        Assertions.assertThat(situasjon.oppstartNyJobb?.verdi).isEqualTo(LocalDate.of(2020, 3, 27))
    }

    @Test
    fun `situasjon label for sagtOppEllerRedusertStilling er mappet`() {
        // Then
        Assertions.assertThat(situasjon.sagtOppEllerRedusertStilling?.label)
                .isEqualTo("Har du sagt opp jobben eller redusert arbeidstiden de siste 6 månedene?")
    }

    @Test
    fun `situasjon verdi for sagtOppEllerRedusertStilling er mappet`() {
        // Then
        Assertions.assertThat(situasjon.sagtOppEllerRedusertStilling?.verdi)
                .isEqualTo("Ja, jeg har sagt opp jobben eller tatt frivillig permisjon, men ikke foreldrepermisjon")
    }

    @Test
    fun `situasjon label for oppsigelseReduksjonTidspunkt er mappet`() {
        // Then
        Assertions.assertThat(situasjon.oppsigelseReduksjonTidspunkt?.label)
                .isEqualTo("sagtOppEllerRedusertStilling.datovelger.sagtOpp")
    }

    @Test
    fun `situasjon verdi for oppsigelseReduksjonTidspunkt er mappet`() {
        // Then
        Assertions.assertThat(situasjon.oppsigelseReduksjonTidspunkt?.verdi).isEqualTo(LocalDate.of(2020, 3, 30))
    }

    @Test
    fun `situasjon label for oppsigelseReduksjonÅrsak er mappet`() {
        // Then
        Assertions.assertThat(situasjon.oppsigelseReduksjonÅrsak?.label).isEqualTo("Hvorfor sa du opp?")
    }

    @Test
    fun `situasjon verdi for oppsigelseReduksjonÅrsak er mappet`() {
        // Then
        Assertions.assertThat(situasjon.oppsigelseReduksjonÅrsak?.verdi).isEqualTo("Ville finne en spennende jobb")
    }


}