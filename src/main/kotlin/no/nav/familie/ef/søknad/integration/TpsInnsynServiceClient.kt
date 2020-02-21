package no.nav.familie.ef.søknad.integration

import no.nav.familie.ef.søknad.config.TpsInnsynConfig
import no.nav.familie.ef.søknad.integration.dto.PersoninfoDto
import no.nav.familie.ef.søknad.integration.dto.RelasjonDto
import no.nav.familie.ef.søknad.util.InnloggingUtils
import no.nav.familie.http.client.AbstractRestClient
import no.nav.familie.log.NavHttpHeaders
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations

@Component
internal class TpsInnsynServiceClient(val tpsInnsynConfig: TpsInnsynConfig,
                                      operations: RestOperations) : AbstractRestClient(operations, "tps.innsyn") {

    fun hentPersoninfo(): PersoninfoDto {
        return getForEntity(tpsInnsynConfig.personUri, httpHeaders())
    }

    fun hentBarn(): List<RelasjonDto> {
        return getForEntity(tpsInnsynConfig.barnUri, httpHeaders())
    }

    private fun httpHeaders(): HttpHeaders {
        return HttpHeaders().apply {
            add(NavHttpHeaders.NAV_PERSONIDENT.asString(), InnloggingUtils.hentFnrFraToken())
        }
    }

}
