package no.nav.familie.ef.soknad.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import no.nav.familie.ef.soknad.api.dto.KvitteringDto
import no.nav.familie.ef.soknad.api.dto.SøknadDto
import no.nav.familie.ef.soknad.integration.SøknadClient
import no.nav.familie.ef.soknad.integration.dto.Kvittering
import no.nav.familie.ef.soknad.mapper.SøknadMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SøknadServiceImplTest {

    private val søknadClient: SøknadClient = mockk()

    private val søknadService = SøknadServiceImpl(søknadClient)

    val søknadDto = SøknadDto("Dette er en søknad")
    val søknad = SøknadMapper.mapTilIntern(søknadDto)
    val kvittering = Kvittering("Dette er en kvittering")

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