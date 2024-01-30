package no.nav.familie.ef.søknad.minside

import com.fasterxml.jackson.module.kotlin.readValue
import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.minside.domain.DokumentoversiktSelvbetjeningResponse
import no.nav.familie.kontrakter.felles.objectMapper
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
        val dummyJournalposter =
            objectMapper.readValue<DokumentoversiktSelvbetjeningResponse>(readFile("dummy-journalposter.json"))
        val safClient: SafClient = mockk()

        every { safClient.ping() } returns Unit

        every { safClient.hentJournalposterForBruker(any()) } returns
                dummyJournalposter

        return safClient
    }

    private fun readFile(filnavn: String): String {
        return this::class.java.getResource("/minside/$filnavn")!!.readText()
    }

}
