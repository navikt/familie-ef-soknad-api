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
        val dokumenter = mapOf("finnes" to listOf(Vedlegg("id1", "dok1.pdf", "Tittel på dok", bytes),
                                                  Vedlegg("id2", "dok2.pdf", "Annen tittel på dok", bytes)))
        val dokumentSomFinnes = dokumentfelt("finnes", dokumenter)!!
        assertThat(dokumentSomFinnes.verdi.first().id).isEqualTo("id1")
        assertThat(dokumentSomFinnes.verdi.first().navn).isEqualTo("dok1.pdf")

        assertThat(dokumentSomFinnes.verdi.last().id).isEqualTo("id2")
        assertThat(dokumentSomFinnes.verdi.last().navn).isEqualTo("dok2.pdf")

        assertThat(dokumentfelt("finnesIkke", dokumenter)).isNull()
    }
}
