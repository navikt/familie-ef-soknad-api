package no.nav.familie.ef.søknad.service

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import no.nav.familie.ef.søknad.config.FamilieDokumentConfig
import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
import no.nav.familie.ef.søknad.integration.FamilieDokumentClient
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.web.client.RestOperations
import java.net.URI


internal class DokumentServiceImplTest {

    private companion object {

        const val VEDLEGG_ID = "id"

        val wireMockServer = WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort())

        @BeforeAll
        @JvmStatic
        fun initClass() {
            wireMockServer.start()
        }

        @AfterAll
        @JvmStatic
        fun tearDown() {
            wireMockServer.stop()
        }
    }

    private val restOperations: RestOperations = RestTemplateBuilder()
            .build()
    private lateinit var client: FamilieDokumentClient
    private lateinit var dokumentService: DokumentService

    @BeforeEach
    internal fun setUp() {
        val config = FamilieDokumentConfig(URI.create(wireMockServer.baseUrl()),
                                           URI.create("http://mocked"))
        client = spyk(FamilieDokumentClient(config, restOperations))
        val featureToggleService = mockk<FeatureToggleService>()
        every { featureToggleService.isEnabled(any()) } returns true
        dokumentService = DokumentServiceImpl(client)
    }


    @Test
    internal fun `skal kalle gcp minst en gang`() {
        val response = WireMock.okJson("{\"status\": \"SUKSESS\", \"data\": \"data\", \"melding\": \"ok\"}")
        wireMockServer.stubFor(WireMock.get(WireMock.anyUrl()).willReturn(response))
        dokumentService.hentVedlegg(VEDLEGG_ID)
        verify(exactly = 1) { client.hentVedlegg(VEDLEGG_ID) }
    }

}
