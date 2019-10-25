package no.nav.familie.ef.søknad.integration

import no.nav.familie.ef.søknad.config.ApplicationConfig
import no.nav.familie.ef.søknad.config.TpsInnsynConfig
import no.nav.familie.ef.søknad.integration.dto.PersoninfoDto
import no.nav.familie.ef.søknad.integration.dto.RelasjonDto
import no.nav.familie.ef.søknad.util.InnloggingUtils
import no.nav.familie.http.client.NavHttpHeaders
import org.eclipse.jetty.http.HttpHeader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestOperations
import org.springframework.web.client.exchange

@Component
internal class TpsInnsynServiceClient @Autowired
constructor(val tpsInnsynConfig: TpsInnsynConfig,
            val applicationConfig: ApplicationConfig,
            operations: RestOperations)
    : PingableRestClient(operations, tpsInnsynConfig.pingUri) {

    fun hentPersoninfo(): PersoninfoDto {
        return getForEntity(tpsInnsynConfig.personUri, httpHeaders())
    }

    fun hentBarn(): List<RelasjonDto> {
        return getForEntity(tpsInnsynConfig.barnUri, httpHeaders())
    }

    override fun ping() {
        val httpHeaders = HttpHeaders().apply {
            add(tpsInnsynConfig.brukernavn, tpsInnsynConfig.passord)
        }
        val respons: ResponseEntity<Any> = operations.exchange(pingUri, HttpMethod.GET, HttpEntity(null, httpHeaders))
        if (!respons.statusCode.is2xxSuccessful) {
            throw HttpServerErrorException(respons.statusCode)
        }
    }

    private fun httpHeaders(): HttpHeaders {
        return HttpHeaders().apply {
            val bearerTokenForLoggedInUser = "Bearer " +InnloggingUtils.getBearerTokenForLoggedInUser()
            add(HttpHeader.AUTHORIZATION.asString(), bearerTokenForLoggedInUser)
            add(tpsInnsynConfig.brukernavn, tpsInnsynConfig.passord)
            log.warn("Vi bruker hardkodet (påkrevd) callId! Dette kan med fordel endres når call-id er klart")
            add(NavHttpHeaders.NAV_CALLID.asString(), "changeMe")
            add(NavHttpHeaders.NAV_PERSONIDENT.asString(), InnloggingUtils.hentFnrFraToken())
            add(NavHttpHeaders.NAV_CONSUMER_ID.asString(), applicationConfig.applicationName)
        }
    }

}
