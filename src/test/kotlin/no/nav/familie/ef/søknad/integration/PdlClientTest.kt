package no.nav.familie.ef.søknad.integration

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.config.PdlConfig
import no.nav.familie.http.sts.StsRestClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.web.client.RestOperations
import java.net.URI

class PdlClientTest {

    private val wireMockServer = WireMockServer(wireMockConfig().dynamicPort())
    private val restOperations: RestOperations = RestTemplateBuilder().build()
    private lateinit var pdlClient: PdlClient

    @BeforeEach
    fun setUp() {
        wireMockServer.start()
        val stsRestClient = mockk<StsRestClient>()
        every { stsRestClient.systemOIDCToken } returns "token"
        pdlClient = PdlClient(PdlConfig(URI.create(wireMockServer.baseUrl()), ""), restOperations, stsRestClient)
    }

    @AfterEach
    fun tearDown() {
        wireMockServer.resetAll()
        wireMockServer.stop()
    }

    @Test
    fun `pdlClient håndterer response for søker-query mot pdl-tjenesten riktig`() {
        wireMockServer.stubFor(post(urlEqualTo("/${PdlConfig.PATH_GRAPHQL}"))
                                       .willReturn(okJson(readFile("søker.json"))))

        val response = pdlClient.hentSøker("")

        assertThat(response.bostedsadresse[0].vegadresse?.adressenavn).isEqualTo("INNGJERDSVEGEN")
    }

    @Test
    fun `pdlClient håndterer response for barn-query mot pdl-tjenesten riktig`() {
        wireMockServer.stubFor(post(urlEqualTo("/${PdlConfig.PATH_GRAPHQL}"))
                                       .willReturn(okJson(readFile("barn.json"))))

        val response = pdlClient.hentBarn(listOf("11111122222"))

        assertThat(response["11111122222"]?.navn?.firstOrNull()?.fornavn).isEqualTo("BRÅKETE")
    }

    private fun readFile(filnavn: String): String {
        return this::class.java.getResource("/pdl/$filnavn").readText()
    }
}
