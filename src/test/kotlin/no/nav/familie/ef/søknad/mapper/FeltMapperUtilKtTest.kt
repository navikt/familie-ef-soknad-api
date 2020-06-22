package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.BooleanFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.DatoFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.ListFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.TekstFelt
import no.nav.familie.kontrakter.ef.søknad.Fødselsnummer
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Vedlegg
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

internal class FeltMapperUtilKtTest {

    @Test
    internal fun `TekstFelt`() {
        val felt = TekstFelt("label", "verdi").tilSøknadsfelt()
        assertEquals(Søknadsfelt("label", "verdi"), felt)
        assertNotEquals(Søknadsfelt("label", "verdi2"), felt)
    }

    @Test
    internal fun `TekstFelt med mapper`() {
        val felt = TekstFelt("label", "08031499039").tilSøknadsfelt(::Fødselsnummer)
        assertEquals(Søknadsfelt("label", Fødselsnummer("08031499039")), felt)
    }

    @Test
    internal fun `BooleanFelt`() {
        val felt = BooleanFelt("label", true).tilSøknadsfelt()
        assertEquals(Søknadsfelt("label", true), felt)
    }

    @Test
    internal fun `DatoFelt`() {
        val felt = DatoFelt("label", LocalDate.MAX).tilSøknadsfelt()
        assertEquals(Søknadsfelt("label", LocalDate.MAX), felt)
    }

    @Test
    internal fun `ListFelt`() {
        val felt = ListFelt("label", listOf("a")).tilSøknadsfelt()
        assertEquals(Søknadsfelt("label", listOf("a")), felt)
    }

    @Test
    internal fun `hent dokumentfelt`() {
        val bytes = byteArrayOf(12)
        val dokumentasjonWrapper = DokumentasjonWrapper("label",
                                                        Søknadsfelt("Har sendt inn tidligere", false),
                                                        listOf(Vedlegg("id1", "dok1.pdf", "Tittel på dok", bytes),
                                                               Vedlegg("id2", "dok2.pdf", "Annen tittel på dok", bytes)))
        val dokumenter = mapOf("finnes" to dokumentasjonWrapper)
        val dokumentSomFinnes = dokumentfelt("finnes", dokumenter)!!
        assertThat(dokumentSomFinnes.verdi.dokumenter.first().id).isEqualTo("id1")
        assertThat(dokumentSomFinnes.verdi.dokumenter.first().navn).isEqualTo("dok1.pdf")

        assertThat(dokumentSomFinnes.verdi.dokumenter.last().id).isEqualTo("id2")
        assertThat(dokumentSomFinnes.verdi.dokumenter.last().navn).isEqualTo("dok2.pdf")

        assertThat(dokumentfelt("finnesIkke", dokumenter)).isNull()
    }

    @Test
    internal fun `Tekst til dato -skal tåle tom streng og returnere null`() {
        val felt = TekstFelt("label", "").tilSøknadsDatoFeltEllerNull()
        assertNull(felt)
    }

    @Test
    internal fun `Tekst til dato -skal gjøre om til dato til streng`() {
        val felt = TekstFelt("label", "2009-11-29").tilSøknadsDatoFeltEllerNull()
        assertEquals(felt?.verdi?.dayOfMonth, 29)
    }

    @Test
    internal fun `Tekst til dato - skal gjøre om til dato med Zulu tid til streng`() {
        val felt = TekstFelt("label", "2020-06-21T19:25:21.752Z").tilSøknadsDatoFeltEllerNull()
        assertEquals(felt?.verdi?.dayOfMonth, 21)
    }

    @Test
    internal fun `Tekst til dato - skal gjøre om til dato med offset og tid til streng`() {
        val felt = TekstFelt("label", "2007-04-05T12:30-02:00").tilSøknadsDatoFeltEllerNull()
        assertEquals(felt?.verdi?.dayOfMonth, 5)
    }
}
