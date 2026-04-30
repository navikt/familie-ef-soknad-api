package no.nav.familie.ef.søknad.mock

import io.mockk.mockk
import no.nav.familie.ef.søknad.kodeverk.KodeverkService
import no.nav.familie.ef.søknad.kodeverk.Landkode
import no.nav.familie.ef.søknad.kodeverk.Spraak
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
            override fun hentLand(landkode: String): String? = "Norge"

            override fun hentPoststed(postnummer: String): String? = "Oslo"

            override fun hentLandkoder(spraak: Spraak): List<Landkode> =
                listOf(
                    Landkode(kode = "BRA", navn = "Brasil", erEøsland = false),
                    Landkode(kode = "DNK", navn = "Danmark", erEøsland = true),
                    Landkode(kode = "DEU", navn = "Tyskland", erEøsland = true),
                    Landkode(kode = "GBR", navn = "Storbritannia", erEøsland = false),
                    Landkode(kode = "NOR", navn = "Norge", erEøsland = true),
                    Landkode(kode = "POL", navn = "Polen", erEøsland = true),
                    Landkode(kode = "SWE", navn = "Sverige", erEøsland = true),
                    Landkode(kode = "USA", navn = "USA", erEøsland = false),
                )
        }
}
