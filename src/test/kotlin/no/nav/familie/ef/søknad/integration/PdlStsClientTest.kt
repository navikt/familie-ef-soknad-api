package no.nav.familie.ef.søknad.integration

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import no.nav.familie.ef.søknad.config.PdlConfig
import no.nav.familie.ef.søknad.exception.PdlRequestException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.web.client.RestOperations
import java.net.URI

class PdlStsClientTest {

    private val wireMockServer = WireMockServer(wireMockConfig().dynamicPort())
    private val restOperations: RestOperations = RestTemplateBuilder().build()
    private lateinit var unsecurePdlStsClient: UnsecurePdlClient

    @BeforeEach
    fun setUp() {
        wireMockServer.start()
        unsecurePdlStsClient = UnsecurePdlClient(PdlConfig(URI.create(wireMockServer.baseUrl()), ""), restOperations)
    }

    @AfterEach
    fun tearDown() {
        wireMockServer.resetAll()
        wireMockServer.stop()
    }

    @Test
    fun `pdlClient håndterer response for barn-query mot pdl-tjenesten riktig`() {
        wireMockServer.stubFor(post(urlEqualTo("/${PdlConfig.PATH_GRAPHQL}"))
                                       .willReturn(okJson(readFile("barn.json"))))

        val response = unsecurePdlStsClient.hentBarn(listOf("11111122222"))

        assertThat(response["11111122222"]?.navn?.firstOrNull()?.fornavn).isEqualTo("BRÅKETE")
    }

    @Test
    fun `pdlClient håndterer response for bolk-query mot pdl-tjenesten der person er null og har errors`() {
        wireMockServer.stubFor(post(urlEqualTo("/${PdlConfig.PATH_GRAPHQL}"))
                                       .willReturn(okJson(readFile("pdlBolkErrorResponse.json"))))
        assertThat(catchThrowable { unsecurePdlStsClient.hentBarn(listOf("")) })
                .hasMessageStartingWith("Feil ved henting av")
                .isInstanceOf(PdlRequestException::class.java)
    }

    @Test
    fun `pdlClient håndterer response for bolk-query mot pdl-tjenesten der data er null og har errors`() {
        wireMockServer.stubFor(post(urlEqualTo("/${PdlConfig.PATH_GRAPHQL}"))
                                       .willReturn(okJson(readFile("pdlBolkErrorResponse_nullData.json"))))
        assertThat(catchThrowable { unsecurePdlStsClient.hentBarn(listOf("")) })
                .hasMessageStartingWith("Data er null fra PDL")
                .isInstanceOf(PdlRequestException::class.java)
    }

    private fun readFile(filnavn: String): String {
        return this::class.java.getResource("/pdl/$filnavn").readText()
    }
}
