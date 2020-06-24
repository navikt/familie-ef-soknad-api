package no.nav.familie.ef.søknad.integration

import no.nav.familie.ef.søknad.config.FamilieIntegrasjonerConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.web.client.RestTemplate
import java.net.URI

internal class FamilieIntegrasjonerClientTest {

    @Test
    internal fun `url er riktig`() {
        val familieIntegrasjonerClient =
                FamilieIntegrasjonerClient(FamilieIntegrasjonerConfig(URI.create("http://familie/test"), ""), RestTemplate())
        assertThat(familieIntegrasjonerClient.poststedUri("0000").toString())
                .isEqualTo("http://familie/test/api/kodeverk/poststed/0000")
        assertThat(familieIntegrasjonerClient.pingUri.toString())
                .isEqualTo("http://familie/test/internal/health/isAlive")
    }
}