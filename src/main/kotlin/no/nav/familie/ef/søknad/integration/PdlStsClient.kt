package no.nav.familie.ef.søknad.integration

import no.nav.familie.ef.søknad.config.PdlConfig
import no.nav.familie.ef.søknad.exception.PdlRequestException
import no.nav.familie.ef.søknad.integration.dto.pdl.*
import no.nav.familie.http.client.AbstractPingableRestClient
import no.nav.familie.http.client.AbstractRestClient
import no.nav.familie.http.sts.StsRestClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations
import org.springframework.web.client.getForEntity
import java.net.URI


@Component
class PdlStsClient(val pdlConfig: PdlConfig,
                   @Qualifier("stsRestKlientMedApiKey") restOperations: RestOperations,
                   private val stsRestClient: StsRestClient)
    : AbstractPingableRestClient(restOperations, "pdl.personinfo") {

    private fun httpHeaders(): HttpHeaders {
        return HttpHeaders().apply {
            add("Nav-Consumer-Token", "Bearer ${stsRestClient.systemOIDCToken}")
            add("Tema", "ENF")
        }
    }

    override val pingUri: URI
        get() = pdlConfig.pdlUri

    override fun ping() {
        operations.headForHeaders(pingUri)
    }
}
