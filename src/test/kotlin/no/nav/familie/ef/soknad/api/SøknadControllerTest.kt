package no.nav.familie.ef.soknad.api

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.soknad.api.dto.KvitteringDto
import no.nav.familie.ef.soknad.api.dto.SøknadDto
import no.nav.familie.ef.soknad.service.SøknadService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SøknadControllerTest {

    private val soknadService: SøknadService = mockk()

    private val søknadController = SøknadController(soknadService)

    @Test
    fun `sendInn returnerer samme kvittering som returneres fra soknadService`() {
        val søknad = SøknadDto("Søknadstekst")
        every { soknadService.sendInn(søknad) } returns KvitteringDto("Mottatt søknad: ${søknad.text}")

        val kvitteringDto = søknadController.sendInn(søknad)

        assertThat(kvitteringDto.text).isEqualTo("Mottatt søknad: ${søknad.text}")
    }
}