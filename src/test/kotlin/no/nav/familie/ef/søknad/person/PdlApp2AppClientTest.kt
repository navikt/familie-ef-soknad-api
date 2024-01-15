package no.nav.familie.ef.søknad.person

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.okJson
import com.github.tomakehurst.wiremock.client.WireMock.post
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import no.nav.familie.ef.søknad.infrastruktur.config.PdlConfig
import no.nav.familie.ef.søknad.infrastruktur.exception.PdlRequestException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.web.client.RestOperations
import java.net.URI

class PdlApp2AppClientTest {

    private val wireMockServer = WireMockServer(wireMockConfig().dynamicPort())
    private val restOperations: RestOperations = RestTemplateBuilder().build()
    private lateinit var pdlApp2AppClient: PdlApp2AppClient

    @BeforeEach
    fun setUp() {
        wireMockServer.start()
        pdlApp2AppClient = PdlApp2AppClient(PdlConfig(URI.create(wireMockServer.baseUrl())), restOperations)
    }

    @AfterEach
    fun tearDown() {
        wireMockServer.resetAll()
        wireMockServer.stop()
    }

    @Test
    fun `pdlClient håndterer response for barn-query mot pdl-tjenesten riktig`() {
        wireMockServer.stubFor(
            post(urlEqualTo("/${PdlConfig.PATH_GRAPHQL}"))
                .willReturn(okJson(readFile("barn.json"))),
        )

        val response = pdlApp2AppClient.hentBarn(listOf("11111122222"))

        assertThat(response["11111122222"]?.navn?.firstOrNull()?.fornavn).isEqualTo("BRÅKETE")
    }

    @Test
    fun `pdlClient håndterer response for bolk-query mot pdl-tjenesten der person er null og har errors`() {
        wireMockServer.stubFor(
            post(urlEqualTo("/${PdlConfig.PATH_GRAPHQL}"))
                .willReturn(okJson(readFile("pdlBolkErrorResponse.json"))),
        )
        assertThat(catchThrowable { pdlApp2AppClient.hentBarn(listOf("")) })
            .hasMessageStartingWith("Feil ved henting av")
            .isInstanceOf(PdlRequestException::class.java)
    }

    @Test
    fun `pdlClient håndterer response for bolk-query mot pdl-tjenesten der data er null og har errors`() {
        wireMockServer.stubFor(
            post(urlEqualTo("/${PdlConfig.PATH_GRAPHQL}"))
                .willReturn(okJson(readFile("pdlBolkErrorResponse_nullData.json"))),
        )
        assertThat(catchThrowable { pdlApp2AppClient.hentBarn(listOf("")) })
            .hasMessageStartingWith("Data er null fra PDL")
            .isInstanceOf(PdlRequestException::class.java)
    }

    private fun readFile(filnavn: String): String {
        return this::class.java.getResource("/pdl/$filnavn").readText()
    }
}
