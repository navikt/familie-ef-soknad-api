package no.nav.familie.ef.søknad.mock

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.integration.SøknadClient
import no.nav.familie.ef.søknad.integration.dto.KvitteringDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile

@Configuration
@Profile("mock-mottak")
class MottakClientMock {


    @Bean
    @Primary
    fun søknadClient(): SøknadClient {
        val søknadClient: SøknadClient = mockk()

        every { søknadClient.sendInn(any()) } returns KvitteringDto("OK MOCK")
        every { søknadClient.sendInnArbeidsRegistreringsskjema(any()) } returns KvitteringDto("OK MOCK")
        every { søknadClient.sendInnBarnetilsynsøknad(any()) } returns KvitteringDto("OK MOCK")
        every { søknadClient.sendInnSkolepenger(any()) } returns KvitteringDto("OK MOCK")
        every { søknadClient.ping() } returns Unit


        return søknadClient
    }


}