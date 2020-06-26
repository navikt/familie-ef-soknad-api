package no.nav.familie.ef.søknad.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import no.nav.familie.ef.søknad.ApplicationLocalLauncher
import no.nav.familie.ef.søknad.integration.FamilieIntegrasjonerClient
import no.nav.familie.kontrakter.felles.kodeverk.BeskrivelseDto
import no.nav.familie.kontrakter.felles.kodeverk.BetydningDto
import no.nav.familie.kontrakter.felles.kodeverk.KodeverkDto
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDate

@Profile("kodeverk-cache-test")
@Configuration
@Primary
class KodeerkTestConfig {

    @Bean
    fun familieIntegrasjonerClient(): FamilieIntegrasjonerClient {
        val mockk = mockk<FamilieIntegrasjonerClient>()
        val poststed = KodeverkDto(mapOf("0575" to listOf(BetydningDto(LocalDate.MIN,
                                                                       LocalDate.MAX,
                                                                       mapOf("nb" to BeskrivelseDto(
                                                                               "Oslo",
                                                                               "Oslo"))))))
        every { mockk.hentKodeverkPoststed() } returns poststed

        return mockk
    }
}

@ActiveProfiles("local", "kodeverk-cache-test")
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [ApplicationLocalLauncher::class])
class KodeverkServiceTest {

    @Autowired lateinit var familieIntegrasjonerClient: FamilieIntegrasjonerClient
    @Autowired lateinit var kodeverkService: KodeverkService

    @Test
    fun `skal cache henting av poststed mot familieIntegrasjonerClient`() {
        kodeverkService.hentPoststed()
        kodeverkService.hentPoststed()
        verify(exactly = 1) { familieIntegrasjonerClient.hentKodeverkPoststed()  }
    }
}
