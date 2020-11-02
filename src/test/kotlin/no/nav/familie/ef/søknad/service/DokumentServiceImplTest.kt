package no.nav.familie.ef.søknad.service

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import no.nav.familie.ef.søknad.config.FamilieDokumentConfig
import no.nav.familie.ef.søknad.integration.FamilieDokumentClient
import no.nav.familie.ef.søknad.integration.FamilieDokumentSbsClient
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
    private val sbsClient = mockk<FamilieDokumentSbsClient>()
    private lateinit var client: FamilieDokumentClient
    private lateinit var dokumentService: DokumentService

    @BeforeEach
    internal fun setUp() {
        val config = FamilieDokumentConfig(URI.create(wireMockServer.baseUrl()),
                                           URI.create("http://mocked"))
        client = spyk(FamilieDokumentClient(config, restOperations))
        every { sbsClient.hentVedlegg(any()) } returns byteArrayOf(12)
        dokumentService = DokumentServiceImpl(client, sbsClient)
    }

    @Test
    internal fun `skal prøve å finne vedlegget i sbs hvis gcp feiler`() {
        wireMockServer.stubFor(WireMock.get(WireMock.anyUrl()).willReturn(WireMock.badRequest()))
        dokumentService.hentVedlegg(VEDLEGG_ID)

        verify(exactly = 1) { client.hentVedlegg(VEDLEGG_ID) }
        verify(exactly = 1) { sbsClient.hentVedlegg(VEDLEGG_ID) }
    }

    @Test
    internal fun `skal ikke kalle sbs hvis vedlegget finnes i gcp`() {
        val response = WireMock.okJson("{\"status\": \"SUKSESS\", \"data\": \"data\", \"melding\": \"ok\"}")
        wireMockServer.stubFor(WireMock.get(WireMock.anyUrl()).willReturn(response))
        dokumentService.hentVedlegg(VEDLEGG_ID)

        verify(exactly = 1) { client.hentVedlegg(VEDLEGG_ID) }
        verify(exactly = 0) { sbsClient.hentVedlegg(VEDLEGG_ID) }
    }

}
