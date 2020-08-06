package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.*
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator
import no.nav.familie.kontrakter.ef.søknad.Fødselsnummer
import no.nav.familie.kontrakter.ef.søknad.Periode
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Vedlegg
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeParseException
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
        val felt = DatoFelt("label", LocalDate.MAX.toString()).tilSøknadsfelt()
        assertEquals(Søknadsfelt("label", LocalDate.MAX), felt)
    }

    @Test
    internal fun `ListFelt`() {
        val felt = ListFelt("label", listOf("a")).tilSøknadsfelt()
        assertEquals(Søknadsfelt("label", listOf("a")), felt)
    }

    @Test
    internal fun `PeriodeFelt`() {
        var felt = PeriodeFelt("label",
                               DatoFelt("fra", "2020-01-01"),
                               DatoFelt("til", "2021-12-30")).tilSøknadsfelt()
        assertEquals(Søknadsfelt("label", Periode(Month.JANUARY, 2020, Month.DECEMBER, 2021)), felt)
    }

    @Test // kan slettes når label er endret til required
    internal fun `PeriodeFelt med null i label`() {
        var felt = PeriodeFelt(null, DatoFelt("fra", "2020-01-01"), DatoFelt("til", "2021-12-30"))
        assertThat(catchThrowable { felt.tilSøknadsfelt() }).isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    internal fun `String til desimaltall`() {
        assertThat(Assertions.catchThrowable { "".tilDesimaltall() }).hasMessage("empty String")
        assertEquals(0.0, "0".tilDesimaltall())
        assertEquals(123.0, "123".tilDesimaltall())
        assertEquals(123.0, "123,".tilDesimaltall())
        assertEquals(123.1, "123.1".tilDesimaltall())
        assertEquals(123.1, "123,1".tilDesimaltall())
        assertEquals(123.12, "123,12".tilDesimaltall())
    }

    @Test
    internal fun `String til heltall`() {
        assertThat(Assertions.catchThrowable { "".tilHeltall() }).hasMessage("empty String")
        assertEquals(0, "0".tilHeltall())
        assertEquals(123, "123".tilHeltall())
        assertEquals(123, "123,".tilHeltall())
        assertEquals(123, "123.1".tilHeltall())
        assertEquals(123, "123,1".tilHeltall())
        assertEquals(123, "123,12".tilHeltall())
        assertEquals(123, "123,99".tilHeltall())
    }

    @Test
    internal fun `hent dokumentfelt`() {
        val bytes = byteArrayOf(12)
        val dokumentasjonWrapper = DokumentasjonWrapper("label",
                                                        Søknadsfelt("Har sendt inn tidligere", false),
                                                        listOf(Vedlegg("id1", "dok1.pdf", "Tittel på dok"),
                                                               Vedlegg("id2", "dok2.pdf", "Annen tittel på dok")))
        val dokumenter = mapOf(DokumentIdentifikator.SYKDOM.name to dokumentasjonWrapper)
        val dokumentSomFinnes = dokumentfelt(DokumentIdentifikator.SYKDOM, dokumenter)!!
        assertThat(dokumentSomFinnes.verdi.dokumenter.first().id).isEqualTo("id1")
        assertThat(dokumentSomFinnes.verdi.dokumenter.first().navn).isEqualTo("dok1.pdf")

        assertThat(dokumentSomFinnes.verdi.dokumenter.last().id).isEqualTo("id2")
        assertThat(dokumentSomFinnes.verdi.dokumenter.last().navn).isEqualTo("dok2.pdf")

        assertThat(dokumentfelt(DokumentIdentifikator.SAMLIVSBRUDD, dokumenter)).isNull()
    }


    /* TekstFelt -> Dato */

    @Test
    internal fun `Tekst til dato -skal tåle tom streng og returnere null`() {
        val felt = DatoFelt("label", "").tilSøknadsDatoFeltEllerNull()
        assertNull(felt)
    }

    @Test
    internal fun `Klokken 22 Zulu, UTC, 31 mai skal bli 1 juni når det er sommertid`() {
        val felt = DatoFelt("label", "2020-05-31T22:00:00.000Z").tilSøknadsDatoFeltEllerNull()
        assertEquals(felt?.verdi, LocalDate.of(2020, 6, 1))
    }

    @Test
    internal fun `Klokken 23, UTC, 3 mars skal bli 4 mars når det er vintertid`() {
        val felt = DatoFelt("label", "2020-03-03T23:00:00.000Z").tilSøknadsDatoFeltEllerNull()
        assertEquals(felt?.verdi, LocalDate.of(2020, 3, 4))
    }


    /* Spesielle tilfeller */

    // Her er jeg usikker på hva som kan ha skjedd - UI feil?
    @Test
    internal fun `Klokken 22, UTC, 3 mars skal bli 3 mars når det er vintertid`() {
        val felt = DatoFelt("label", "2020-03-03T22:00:00.000Z").tilSøknadsDatoFeltEllerNull()
        assertEquals(felt?.verdi, LocalDate.of(2020, 3, 3))
    }


    // Skal egentlig ikke skje - skulle vi kastet exception?
    @Test
    internal fun `Tekst til dato - tid uten zulu`() {
        val felt = DatoFelt("label", "2020-06-21T23:00:00.000").tilSøknadsDatoFeltEllerNull()
        assertEquals(felt?.verdi?.dayOfMonth, 21)
    }

    // Vi kan ikke parse iso datoer med offset
    @Test
    internal fun `Tekst til dato - skal gjøre om tekst med zone data`() {
        assertThrows<DateTimeParseException> { DatoFelt("label", "2007-04-05T23:59+02:00").tilSøknadsDatoFeltEllerNull() }
    }
}
