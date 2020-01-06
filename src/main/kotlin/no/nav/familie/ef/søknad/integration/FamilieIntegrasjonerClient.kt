package no.nav.familie.ef.søknad.integration

import no.nav.familie.ef.søknad.config.ApplicationConfig
import no.nav.familie.ef.søknad.config.FamilieIntegrasjonerConfig
import no.nav.familie.ef.søknad.util.URIUtil
import no.nav.familie.http.client.NavHttpHeaders
import no.nav.familie.log.mdc.MDCConstants.MDC_CALL_ID
import org.slf4j.MDC
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations

@Component
internal class FamilieIntegrasjonerClient(val config: FamilieIntegrasjonerConfig,
                                          val applicationConfig: ApplicationConfig,
                                          operations: RestOperations) : AbstractRestClient(operations) {
    fun hentPoststedFor(postnummer: String): String {
        return getForEntity(URIUtil.uri(config.poststedUri, postnummer), httpHeaders())
    }

    private fun httpHeaders(): HttpHeaders {
        return HttpHeaders().apply {
            add(NavHttpHeaders.NAV_CALLID.asString(), MDC.get(MDC_CALL_ID))
            add(NavHttpHeaders.NAV_CONSUMER_ID.asString(), applicationConfig.applicationName)
        }
    }

}
