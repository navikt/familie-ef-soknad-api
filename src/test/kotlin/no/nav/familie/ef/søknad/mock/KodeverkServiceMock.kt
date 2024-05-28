package no.nav.familie.ef.søknad.mock

import io.mockk.mockk
import no.nav.familie.ef.søknad.kodeverk.KodeverkService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile

@Configuration
@Profile("mock-kodeverk")
class KodeverkServiceMock {
    @Bean
    @Primary
    fun kodeverkService(): KodeverkService =
        object : KodeverkService(mockk()) {
            override fun hentLand(landkode: String): String? {
                return "Norge"
            }

            override fun hentPoststed(postnummer: String): String? {
                return "Oslo"
            }
        }
}
