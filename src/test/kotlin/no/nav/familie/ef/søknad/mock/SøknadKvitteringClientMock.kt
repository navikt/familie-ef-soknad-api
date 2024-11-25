package no.nav.familie.ef.søknad.mock

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.søknad.SøknadKvitteringClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile

@Configuration
@Profile("mock-søknadkvittering")
class SøknadKvitteringClientMock {
    @Bean
    @Primary
    fun søknadKvitteringClient(): SøknadKvitteringClient {
        val søknadKvitteringClient = mockk<SøknadKvitteringClient>()

        every { søknadKvitteringClient.hentSøknadKvittering(any()) } returns "pdf".toByteArray()
        return søknadKvitteringClient
    }
}
