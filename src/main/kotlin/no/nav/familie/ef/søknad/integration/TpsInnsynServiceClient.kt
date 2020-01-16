package no.nav.familie.ef.søknad.integration

import no.nav.familie.ef.søknad.config.ApplicationConfig
import no.nav.familie.ef.søknad.config.TpsInnsynConfig
import no.nav.familie.ef.søknad.integration.dto.PersoninfoDto
import no.nav.familie.ef.søknad.integration.dto.RelasjonDto
import no.nav.familie.ef.søknad.util.InnloggingUtils
import no.nav.familie.log.NavHttpHeaders
import no.nav.familie.log.mdc.MDCConstants.MDC_CALL_ID
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations

@Component
internal class TpsInnsynServiceClient @Autowired
constructor(val tpsInnsynConfig: TpsInnsynConfig,
            val applicationConfig: ApplicationConfig,
            operations: RestOperations)
    : AbstractRestClient(operations) {

    fun hentPersoninfo(): PersoninfoDto {
        return getForEntity(tpsInnsynConfig.personUri, httpHeaders())
    }

    fun hentBarn(): List<RelasjonDto> {
        return getForEntity(tpsInnsynConfig.barnUri, httpHeaders())
    }

    private fun httpHeaders(): HttpHeaders {
        return HttpHeaders().apply {
            add(NavHttpHeaders.NAV_CALL_ID.asString(), MDC.get(MDC_CALL_ID))
            add(NavHttpHeaders.NAV_PERSONIDENT.asString(), InnloggingUtils.hentFnrFraToken())
            add(NavHttpHeaders.NAV_CONSUMER_ID.asString(), applicationConfig.applicationName)
        }
    }

}
