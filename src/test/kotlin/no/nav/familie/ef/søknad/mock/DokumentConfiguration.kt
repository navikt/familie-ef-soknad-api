package no.nav.familie.ef.søknad.mock

import no.nav.familie.ef.søknad.service.DokumentService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile

@Profile("mock-dokument")
@Configuration
class DokumentConfiguration {

    @Bean("dokumentlager")
    fun dokumenter(): Map<String, ByteArray> = mutableMapOf()

    @Bean
    @Primary
    fun dokumentService(@Qualifier("dokumentlager") dokumenter: Map<String, ByteArray>): DokumentService =
            object : DokumentService {
                override fun hentVedlegg(vedleggsId: String): ByteArray = dokumenter[vedleggsId] ?: error("Finner ikke vedlegg")
            }
}
