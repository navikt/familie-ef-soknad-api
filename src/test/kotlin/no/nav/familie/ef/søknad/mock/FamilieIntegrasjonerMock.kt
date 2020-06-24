package no.nav.familie.ef.søknad.mock

import io.mockk.mockk
import no.nav.familie.ef.søknad.integration.FamilieIntegrasjonerClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile

@Configuration
@Profile("mock-familie-integrasjoner")
class FamilieIntegrasjonerMock {

    @Bean
    @Primary
    fun familieIntegrasjonerClient(): FamilieIntegrasjonerClient =
            object : FamilieIntegrasjonerClient(mockk(relaxed = true),
                                                mockk(relaxed = true)) {
                override fun hentPoststedFor(postnummer: String): String? {
                    return "Oslo"
                }
            }

}
