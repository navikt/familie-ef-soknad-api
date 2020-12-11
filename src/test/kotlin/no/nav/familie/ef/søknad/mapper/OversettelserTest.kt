package no.nav.familie.ef.søknad.mapper

import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.familie.kontrakter.felles.objectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.context.i18n.LocaleContextHolder
import java.io.File
import java.util.*

internal class OversettelserTest {


    @Test
    fun `Skal returnere key hvis oversettelse ikke finnes`() {
        val expected = "KeySomIkkeFinnes"
        val hentetTekst = Oversettelser.hentTekst(expected)
        assertThat(hentetTekst).isEqualTo(expected)
        assertThat(hentetTekst).isEqualTo(expected.hentTekst())
    }

    @Test
    fun `Skal returnere riktig tekst hvis oversettelse finnes`() {
        val expected = "TestValue"
        kontekst.set("nb")
        val hentTekst = expected.hentTekst()
        assertThat(hentTekst).isEqualTo("Noe")
    }

    @Test
    fun `Skal returnere riktig tekst hvis oversettelse finnes på engelsk`() {
        val expected = "TestValue"
        LocaleContextHolder.setLocale(Locale("en"))
        kontekst.set("en")

        val hentTekst = expected.hentTekst()
        assertThat(hentTekst).isEqualTo("Something")
    }

    @Test
    fun `Fil med tekster skal ha to språk og samme antall nøkler`() {
        val tekst: Map<String, Map<String, String>> = objectMapper.readValue(File("src/main/resources/tekst/tekst.json"))
        assertThat(tekst).hasSize(2)
        val norsk = tekst["nb"]
        val engelsk = tekst["en"]
        assertThat(norsk!!.keys.size).isEqualTo(engelsk!!.keys.size)
    }

    @Test
    fun `Fil med tekster skal ha to språk og samme nøkler`() {
        val tekst: Map<String, Map<String, String>> = objectMapper.readValue(File("src/main/resources/tekst/tekst.json"))
        assertThat(tekst).hasSize(2)
        val norsk = tekst["nb"]
        val engelsk = tekst["en"]
        assertThat(norsk!!.keys).isEqualTo(engelsk!!.keys)
    }

    @AfterEach
    fun clear() {
        kontekst.remove()
    }

}