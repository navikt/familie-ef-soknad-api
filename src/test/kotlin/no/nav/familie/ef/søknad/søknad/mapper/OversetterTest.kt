package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.utils.Språk
import no.nav.familie.ef.søknad.utils.Språktekster
import no.nav.familie.ef.søknad.utils.hentTekst
import no.nav.familie.ef.søknad.utils.kontekst
import no.nav.familie.ef.søknad.utils.språk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

internal class OversetterTest {
    @Test
    fun `Skal mappe kunne sette, hente og mappe en enum`() {
        val språk = språk()
        assertThat(språk).isEqualTo(Språk.NB)
        val en = Språk.fromString("en")
        kontekst.set(en)
        val språkEn = språk()
        assertThat(språkEn).isEqualTo(Språk.EN)
    }

    @Test
    fun `Skal returnere riktig tekst hvis oversettelse finnes`() {
        val expected = Språktekster.Fødselsnummer
        kontekst.set(Språk.fromString("nb"))
        val hentTekst = expected.hentTekst()
        assertThat(hentTekst).isEqualTo("Fødselsnummer")
    }

    @Test
    fun `Skal returnere riktig tekst hvis oversettelse finnes på engelsk`() {
        val expected = Språktekster.Fødselsnummer
        kontekst.set(Språk.fromString("en"))
        val hentTekst = expected.hentTekst()
        assertThat(hentTekst).isEqualTo("National identity number or D number")
    }

    @AfterEach
    fun clear() {
        kontekst.remove()
    }
}
