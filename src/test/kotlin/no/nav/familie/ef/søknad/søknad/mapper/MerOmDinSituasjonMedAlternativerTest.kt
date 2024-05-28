package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.mock.søknadDto
import no.nav.familie.ef.søknad.søknad.domain.Situasjon
import no.nav.familie.ef.søknad.søknad.dto.SøknadOvergangsstønadDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MerOmDinSituasjonMedAlternativerTest {
    // Gitt
    private val søknad = søknadDto()

    // Når
    private val situasjon = SituasjonsMapper.map(søknad, mapOf())

    private val forventet =
        listOf(
            "Jeg er syk",
            "Barnet mitt er sykt",
            "Jeg har søkt om barnepass, men ikke fått plass enda",
            "Jeg har barn som trenger særlig tilsyn på grunn av fysiske, psykiske eller store sosiale problemer",
            "Nei",
        )

    @Test
    fun `Situasjon - gjelder dette deg har alternativer`() {
        val alternativer = situasjon.verdi.gjelderDetteDeg.alternativer
        assertThat(alternativer).isNotNull
        assertThat(alternativer).isEqualTo(forventet)
    }

    @Test
    fun `Situasjon - andre felter har ikke har alternativer`() {
        val arbeidskontraktAlternativer = situasjon.verdi.arbeidskontrakt?.alternativer
        val situasjonAlternativer = situasjon.alternativer
        assertThat(arbeidskontraktAlternativer).isNull()
        assertThat(situasjonAlternativer).isNull()
    }

    @Test
    fun `Situasjon - endrede alternativer mappes`() {
        val nyListe = listOf("Nja", "Tja")
        val søknadMedNyeAlternativer =
            copySøknad(nyListe)
        val nySituasjon = SituasjonsMapper.map(søknadMedNyeAlternativer, mapOf())
        assertThat(nySituasjon.verdi.gjelderDetteDeg.alternativer).isEqualTo(nyListe)
    }

    private fun copySøknad(nyListe: List<String>): SøknadOvergangsstønadDto {
        return søknad.copy(merOmDinSituasjon = copyMerOmDinSituasjon(nyListe))
    }

    private fun copyMerOmDinSituasjon(nyListe: List<String>): Situasjon {
        return søknad.merOmDinSituasjon.copy(gjelderDetteDeg = copyGjelderDetteDeg(nyListe))
    }

    private fun copyGjelderDetteDeg(nyListe: List<String>) =
        søknad.merOmDinSituasjon.gjelderDetteDeg.copy(
            alternativer = nyListe,
        )
}
