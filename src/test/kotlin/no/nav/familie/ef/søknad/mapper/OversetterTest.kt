package no.nav.familie.ef.søknad.mapper

import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.familie.kontrakter.felles.objectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.io.File

internal class OversetterTest {


    @Test
    fun `Skal mappe kunne sette hente og mappe en enum`() {

        val språk = språk()
        assertThat(språk).isEqualTo(Språk.NB)
        val en = Språk.fromString("en")
        kontekst.set(en)
        val språkEn = språk()
        assertThat(språkEn).isEqualTo(Språk.EN)

        val tekst: String = this::class.java.classLoader.getResource("tekst/tekst.json").readText()
        val tekstMap: Map<Språk, Map<String, String>> = objectMapper.readValue(tekst)
        assertThat(tekstMap.keys.size).isEqualTo(2)
        val alleSpråk: Set<Språk> = setOf(Språk.EN, Språk.NB)
        val keys: Set<Språk> = tekstMap.keys
        assertThat(keys).containsAll(alleSpråk)
    }


    @Test
    fun `Skal returnere key hvis oversettelse ikke finnes`() {
        val expected = "KeySomIkkeFinnes"
        val hentetTekst = Oversetter.hentTekst(expected)
        assertThat(hentetTekst).isEqualTo(expected)
        assertThat(hentetTekst).isEqualTo(expected.hentTekst())
    }

    @Test
    fun `Skal returnere riktig tekst hvis oversettelse finnes`() {
        val expected = "Fødselsnummer"

        kontekst.set(Språk.fromString("nb"))

        val hentTekst = expected.hentTekst()
        assertThat(hentTekst).isEqualTo("Fødselsnummer")
    }

    @Test
    fun `Skal returnere riktig tekst hvis oversettelse finnes på engelsk`() {
        val expected = "Fødselsnummer"
        kontekst.set(Språk.fromString("en"))
        val hentTekst = expected.hentTekst()
        assertThat(hentTekst).isEqualTo("National identity number or D number")
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