package no.nav.familie.ef.søknad.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import no.nav.familie.ef.søknad.api.dto.KvitteringDto
import no.nav.familie.ef.søknad.api.dto.SøknadDto
import no.nav.familie.ef.søknad.integration.SøknadClient
import no.nav.familie.ef.søknad.integration.dto.Kvittering
import no.nav.familie.ef.søknad.mapper.SøknadMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SøknadServiceImplTest {

    private val søknadClient: SøknadClient = mockk()

    private val søknadService = SøknadServiceImpl(søknadClient)

    private val søknadDto = SøknadDto("Dette er en søknad")
    private val søknad = SøknadMapper.mapTilIntern(søknadDto)
    private val kvittering = Kvittering("Dette er en kvittering")

    @BeforeEach
    private fun init() {
        every { søknadClient.sendInn(søknad) } returns kvittering
    }


    @Test
    fun `sendInn skal kalle søknadClient for å sende inn søknad`() {
        søknadService.sendInn(søknadDto)

        verify { søknadClient.sendInn(søknad) }
    }

    @Test
    fun `sendInn skal returnere kvitteringDto basert på kvittering fra søknadClient`() {
        val kvitteringDto = søknadService.sendInn(søknadDto)

        assertThat(kvitteringDto).isEqualTo(KvitteringDto(kvittering.text))
    }

}