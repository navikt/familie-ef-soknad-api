package no.nav.familie.ef.søknad.integration

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.okJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
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

class PdlClientTest {

    private val wireMockServer = WireMockServer(wireMockConfig().dynamicPort())
    private val restOperations: RestOperations = RestTemplateBuilder().build()
    private lateinit var pdlClient: PdlClient

    @BeforeEach
    fun setUp() {
        wireMockServer.start()
        pdlClient = PdlClient(PdlConfig(URI.create(wireMockServer.baseUrl())), restOperations)
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
    fun `pdlClient håndterer response for søker-query mot pdl-tjenesten der person i data er null`() {
        wireMockServer.stubFor(post(urlEqualTo("/${PdlConfig.PATH_GRAPHQL}"))
                                       .willReturn(okJson("{\"data\": {}}")))
        assertThat(catchThrowable { pdlClient.hentSøker("") })
                .hasMessageStartingWith("Manglende ")
                .isInstanceOf(PdlRequestException::class.java)
    }

    @Test
    fun `pdlClient håndterer response for søker-query mot pdl-tjenesten der data er null og har errors`() {
        wireMockServer.stubFor(post(urlEqualTo("/${PdlConfig.PATH_GRAPHQL}"))
                                       .willReturn(okJson(readFile("pdlErrorResponse.json"))))
        assertThat(catchThrowable { pdlClient.hentSøker("") })
                .hasMessageStartingWith("Feil ved henting av")
                .isInstanceOf(PdlRequestException::class.java)
    }

    private fun readFile(filnavn: String): String {
        return this::class.java.getResource("/pdl/$filnavn").readText()
    }
}
