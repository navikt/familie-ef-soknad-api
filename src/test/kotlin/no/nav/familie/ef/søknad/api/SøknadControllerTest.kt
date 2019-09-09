package no.nav.familie.ef.søknad.api

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.api.dto.KvitteringDto
import no.nav.familie.ef.søknad.api.dto.SøknadDto
import no.nav.familie.ef.søknad.service.SøknadService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SøknadControllerTest {

    private val søknadService: SøknadService = mockk()

    private val søknadController = SøknadController(søknadService)

    @Test
    fun `sendInn returnerer samme kvittering som returneres fra søknadService`() {
        val søknad = SøknadDto("tekst")
        every { søknadService.sendInn(søknad) } returns KvitteringDto("Mottatt søknad: ${søknad.text}")

        val kvitteringDto = søknadController.sendInn(søknad)

        assertThat(kvitteringDto.text).isEqualTo("Mottatt søknad: ${søknad.text}")
    }
}