package no.nav.familie.ef.søknad.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import no.nav.familie.ef.søknad.integrationTest.OppslagSpringRunnerTest
import no.nav.familie.ef.søknad.infrastruktur.kodeverk.FamilieIntegrasjonerClient
import no.nav.familie.ef.søknad.infrastruktur.kodeverk.KodeverkService
import no.nav.familie.kontrakter.felles.kodeverk.BeskrivelseDto
import no.nav.familie.kontrakter.felles.kodeverk.BetydningDto
import no.nav.familie.kontrakter.felles.kodeverk.KodeverkDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import java.time.LocalDate

@Profile("kodeverk-cache-test")
@Configuration
@Primary
class KodeverkTestConfig {

    @Bean
    fun familieIntegrasjonerClient(): FamilieIntegrasjonerClient {
        val mockk = mockk<FamilieIntegrasjonerClient>()
        every { mockk.hentKodeverkPoststed() } returns kodeverk("0575", "Oslo")
        every { mockk.hentKodeverkLandkoder() } returns kodeverk("NOR", "NORGE")

        return mockk
    }

    private fun kodeverk(kode: String, verdi: String): KodeverkDto {
        return KodeverkDto(
            mapOf(
                kode to listOf(
                    BetydningDto(
                        LocalDate.MIN,
                        LocalDate.MAX,
                        mapOf("nb" to BeskrivelseDto(verdi, verdi)),
                    ),
                ),
            ),
        )
    }
}

class KodeverkServiceTest : OppslagSpringRunnerTest() {

    @Autowired
    lateinit var familieIntegrasjonerClient: FamilieIntegrasjonerClient

    @Autowired
    lateinit var cachedKodeServ: KodeverkService.CachedKodeverkService

    @Test
    fun `skal cache henting av poststed mot familieIntegrasjonerClient`() {
        val kodeverkService = KodeverkService(cachedKodeverkService = cachedKodeServ)
        kodeverkService.hentPoststed("0575")
        kodeverkService.hentPoststed("0575")
        verify(exactly = 1) { familieIntegrasjonerClient.hentKodeverkPoststed() }
    }

    @Test
    fun `skal cache henting av land mot familieIntegrasjonerClient`() {
        val kodeverkService = KodeverkService(cachedKodeverkService = cachedKodeServ)
        kodeverkService.hentLand("NOR")
        kodeverkService.hentLand("SWE")
        verify(exactly = 1) { familieIntegrasjonerClient.hentKodeverkLandkoder() }
    }
}
