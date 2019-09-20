package no.nav.familie.ef.søknad.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.api.dto.Søknad
import no.nav.familie.ef.søknad.integration.SøknadClient
import no.nav.familie.ef.søknad.integration.dto.KvitteringDto
import no.nav.familie.ef.søknad.mapper.SøknadMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SøknadDtoServiceImplTest {

    private val søknadClient: SøknadClient = mockk()

    private val søknadService = SøknadServiceImpl(søknadClient)

    private val søknadDto = Søknad("Dette er en søknad")
    private val søknad = SøknadMapper.mapTilIntern(søknadDto)
    private val kvittering = KvitteringDto("Dette er en kvittering")

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

        assertThat(kvitteringDto).isEqualTo(Kvittering(kvittering.text))
    }

}