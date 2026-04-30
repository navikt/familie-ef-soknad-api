package no.nav.familie.ef.søknad.person

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.kodeverk.KodeverkService
import no.nav.familie.ef.søknad.kodeverk.Landkode
import no.nav.familie.ef.søknad.kodeverk.Språk
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class OppslagControllerTest {
    private val oppslagService: OppslagService = mockk()
    private val kodeverkService: KodeverkService = mockk()
    private val oppslagsController =
        OppslagController(
            oppslagService = oppslagService,
            kodeverkService = kodeverkService,
        )

    @Test
    fun `Feiler med ulovlig (tekst) input`() {
        assertFailsWith(IllegalArgumentException::class) {
            oppslagsController.postnummer("tekstErRikkeLov")
        }
    }

    @Test
    fun `Feiler med negativt tall input`() {
        assertFailsWith(IllegalArgumentException::class) {
            oppslagsController.postnummer("-123")
        }
    }

    @Test
    fun `Feiler med ulovlig (for lang) input`() {
        assertFailsWith(IllegalArgumentException::class) {
            oppslagsController.postnummer("12345")
        }
    }

    @Test
    fun `Feiler med ulovlig (for kort) input`() {
        assertFailsWith(IllegalArgumentException::class) {
            oppslagsController.postnummer("123")
        }
    }

    @Test
    fun `Feiler ikke med gyldig verdi`() {
        every { kodeverkService.hentPoststed("9700") } returns "Lakselv"
        val poststed = oppslagsController.postnummer("9700")
        assertEquals(actual = poststed.body, expected = "Lakselv")
    }

    @Test
    fun `landkoder delegerer til kodeverkService med default språk nb`() {
        val forventetSpråk = listOf(Landkode(kode = "NOR", navn = "Norge", erEøsland = true))
        every { kodeverkService.hentLandkoder(Språk.NB) } returns forventetSpråk

        val land = oppslagsController.landkoder(språk = "nb")

        assertEquals(actual = land, expected = forventetSpråk)
    }

    @Test
    fun `landkoder aksepterer nn og en`() {
        every { kodeverkService.hentLandkoder(Språk.NN) } returns emptyList()
        every { kodeverkService.hentLandkoder(Språk.EN) } returns emptyList()

        oppslagsController.landkoder(språk = "nn")
        oppslagsController.landkoder(språk = "en")
    }

    @Test
    fun `landkoder feiler med 400 ved ulovlig språk`() {
        val exception =
            assertFailsWith(ResponseStatusException::class) {
                oppslagsController.landkoder(språk = "tysk")
            }
        assertEquals(actual = exception.statusCode, expected = HttpStatus.BAD_REQUEST)
    }
}
