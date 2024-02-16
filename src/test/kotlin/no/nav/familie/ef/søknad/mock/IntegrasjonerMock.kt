package no.nav.familie.ef.søknad.mock

import io.mockk.justRun
import io.mockk.mockk
import no.nav.familie.ef.søknad.kodeverk.FamilieIntegrasjonerClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile

@Configuration
@Profile("mock-integrasjoner")
class IntegrasjonerMock {

    @Bean
    @Primary
    fun familieIntegrasjonerClient(): FamilieIntegrasjonerClient {
        val søknadClient: FamilieIntegrasjonerClient = mockk()
        justRun { søknadClient.ping() }
        return søknadClient
    }
}
