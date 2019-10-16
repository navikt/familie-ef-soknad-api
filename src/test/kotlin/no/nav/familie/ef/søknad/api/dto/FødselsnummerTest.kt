package no.nav.familie.ef.søknad.api.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class FødselsnummerTest {

    @Test
    fun `Gyldig fødselsnummer gir verdi lik fødselsnummer`() {
            val fødselsnummer = Fødselsnummer("28047800120")

            assertThat(fødselsnummer.verdi).isEqualTo("28047800120")
    }

    @Test
    fun `individnummer mellom 000 og 499 gir fødselsår på 1900-tallet`() {
        val array = arrayOf("28047800120", "28047849928")

        array.forEach {
            val fødselsnummer = Fødselsnummer(it)

            assertThat(fødselsnummer.fødselsdato.year).isEqualTo(1900 + it.substring(4, 6).toInt())
        }
    }

    @Test
    fun `individnummer  mellom 500 og 749 gir fødselsår på 1800-tallet hvis født mellom 54 og 99`() {
        val array = arrayOf("28045450033", "28045474919", "28047850063", "28047874949", "28049950030", "28049974916")

        array.forEach {
            val fødselsnummer = Fødselsnummer(it)

            assertThat(fødselsnummer.fødselsdato.year).isEqualTo(1800 + it.substring(4, 6).toInt())
        }
    }

    @Test
    fun `individnummer mellom 900 og 999 gir fødselsår på 1900-tallet hvis født mellom 40 og 99`() {
        val array = arrayOf("28044090092", "28044099839", "28047890014", "28047899941", "28049990091", "28049999919")

        array.forEach {
            val fødselsnummer = Fødselsnummer(it)

            assertThat(fødselsnummer.fødselsdato.year).isEqualTo(1900 + it.substring(4, 6).toInt())
        }
    }

    @Test
    fun `individnummer mellom 500 og 999 gir fødselsår på 2000-tallet hvis født mellom 00 og 39`() {
        val array = arrayOf("28040050029", "28040099907", "28043999879", "28043950071", "28042850022", "28042899900")

        array.forEach {
            val fødselsnummer = Fødselsnummer(it)

            assertThat(fødselsnummer.fødselsdato.year).isEqualTo(2000 + it.substring(4, 6).toInt())
        }
    }

    @Test
    fun `D-nummer gir erDNummer true`() {
        val fødselsnummer = Fødselsnummer("68047800033")

        assertThat(fødselsnummer.erDNummer).isTrue()
    }

    @Test
    fun `Fødselsnummer gir erDNummer false`() {
        val fødselsnummer = Fødselsnummer("28043999879")

        assertThat(fødselsnummer.erDNummer).isFalse()
    }
}