package no.nav.familie.ef.søknad.mapper

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.context.i18n.LocaleContextHolder
import java.util.*

internal class OversettelserTest {


    @Test
    fun `Skal returnere key hvis oversettelse ikke finnes`() {
        val expected = "KeySomIkkeFinnes"
        val hentetTekst = Oversettelser.hentTekst(expected)
        Assertions.assertThat(hentetTekst).isEqualTo(expected)
        Assertions.assertThat(hentetTekst).isEqualTo(expected.hentTekst())
    }

    @Test
    fun `Skal returnere riktig tekst hvis oversettelse finnes`() {
        val expected = "TestValue"
        LocaleContextHolder.setLocale(Locale("nb"))
        val hentTekst = expected.hentTekst()
        Assertions.assertThat(hentTekst).isEqualTo("Noe")
    }

    @Test
    fun `Skal returnere riktig tekst hvis oversettelse finnes på engelsk`() {
        val expected = "TestValue"
        LocaleContextHolder.setLocale(Locale("en"))
        val hentTekst = expected.hentTekst()
        Assertions.assertThat(hentTekst).isEqualTo("Something")

    }

    @AfterEach
    fun clear() {
        LocaleContextHolder.resetLocaleContext()
    }

}