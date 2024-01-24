package no.nav.familie.ef.søknad.minside

import io.mockk.every
import io.mockk.mockk
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile

@Configuration
@Profile("mock-saf")
class SafClientConfig {

    @Bean
    @Primary
    fun safClient(): SafClient {
        val safClient: SafClient = mockk()

        every { safClient.ping() } returns Unit

        every { safClient.hentJournalposterForBruker(any()) } returns
                emptyList()

        return safClient
    }
}
