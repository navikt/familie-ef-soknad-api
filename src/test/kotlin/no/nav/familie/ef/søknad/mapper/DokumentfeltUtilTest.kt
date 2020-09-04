package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.mapper.DokumentfeltUtil.dokumentfelt
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Vedlegg
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DokumentfeltUtilTest {

    @Test
    internal fun `hent dokumentfelt med 2 vedlegg`() {
        val dokumentasjonWrapper = DokumentasjonWrapper("label",
                                                        Søknadsfelt("Har sendt inn tidligere", false),
                                                        listOf(Vedlegg("id1", "dok1.pdf", "Tittel på dok"),
                                                               Vedlegg("id2", "dok2.pdf", "Annen tittel på dok")))
        val vedleggMap = mapOf(DokumentIdentifikator.SYKDOM.name to dokumentasjonWrapper)

        val dokumenter = dokumentfelt(DokumentIdentifikator.SYKDOM, vedleggMap)!!

        assertThat(dokumenter.verdi.dokumenter.first().id).isEqualTo("id1")
        assertThat(dokumenter.verdi.dokumenter.first().navn).isEqualTo("dok1.pdf")

        assertThat(dokumenter.verdi.dokumenter.last().id).isEqualTo("id2")
        assertThat(dokumenter.verdi.dokumenter.last().navn).isEqualTo("dok2.pdf")

        assertThat(dokumentfelt(DokumentIdentifikator.SAMLIVSBRUDD, vedleggMap)).isNull()
    }

    @Test
    internal fun `hent dokumentfelt med har sendt inn tidligere`() {
        val dokumentasjonWrapper = DokumentasjonWrapper("label",
                                                        Søknadsfelt("Har sendt inn tidligere", true),
                                                        listOf())
        val vedleggMap = mapOf(DokumentIdentifikator.SYKDOM.name to dokumentasjonWrapper)

        val dokumenter = dokumentfelt(DokumentIdentifikator.SYKDOM, vedleggMap)!!

        assertThat(dokumenter.verdi.harSendtInnTidligere.verdi).isTrue()
        assertThat(dokumenter.verdi.dokumenter).isEmpty()
    }

    @Test
    internal fun `hent dokumentfelt med 0 vedlegg`() {
        val vedleggMap = emptyMap<String, DokumentasjonWrapper>()

        val dokumenter = dokumentfelt(DokumentIdentifikator.SYKDOM, vedleggMap)
        assertThat(dokumenter).isNull()
    }

}
