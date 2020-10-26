package no.nav.familie.ef.søknad.integration

import no.nav.familie.ef.søknad.config.PdlConfig
import no.nav.familie.http.client.AbstractRestClient
import no.nav.familie.http.sts.StsRestClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations


@Component
class PdlStsClient(val pdlConfig: PdlConfig,
                   @Qualifier("stsRestKlientMedApiKey") restOperations: RestOperations,
                   private val stsRestClient: StsRestClient)
    : AbstractRestClient(restOperations, "pdl.personinfo") {

    private fun httpHeaders(): HttpHeaders {
        return HttpHeaders().apply {
            add("Nav-Consumer-Token", "Bearer ${stsRestClient.systemOIDCToken}")
            add("Tema", "ENF")
        }
    }

}
