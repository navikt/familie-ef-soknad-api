package no.nav.familie.ef.søknad.api

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.service.KodeverkService
import no.nav.familie.ef.søknad.service.OppslagService
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class OppslagControllerTest {

    private val oppslagService: OppslagService = mockk()
    private val kodeverkService: KodeverkService = mockk()
    private val oppslagsController = OppslagController(
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
}
