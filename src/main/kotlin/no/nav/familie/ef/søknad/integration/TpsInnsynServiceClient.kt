package no.nav.familie.ef.søknad.integration

import no.nav.familie.ef.søknad.config.ApplicationConfig
import no.nav.familie.ef.søknad.config.TpsInnsynConfig
import no.nav.familie.ef.søknad.integration.dto.PersoninfoDto
import no.nav.familie.ef.søknad.integration.dto.RelasjonDto
import no.nav.familie.ef.søknad.util.InnloggingUtils
import no.nav.familie.http.client.NavHttpHeaders
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
        val httpHeaders = httpHeaders()
        return getForEntity(tpsInnsynConfig.personUri, httpHeaders)
    }

    fun hentBarn(): List<RelasjonDto> {
        return getForEntity(tpsInnsynConfig.barnUri, httpHeaders())
    }

    override fun ping() {
        val httpHeaders = HttpHeaders().apply {
            add(tpsInnsynConfig.bruker, tpsInnsynConfig.passord)
        }
        val respons: ResponseEntity<Any> = operations.exchange(pingUri, HttpMethod.GET, HttpEntity(null, httpHeaders))
        if (!respons.statusCode.is2xxSuccessful) {
            throw HttpServerErrorException(respons.statusCode)
        }
    }

    private fun httpHeaders(): HttpHeaders {

        log.info("tpsInnsyn bruker: " + tpsInnsynConfig.bruker)
        log.info("PersonIdent: " +NavHttpHeaders.NAV_PERSONIDENT.asString())
        log.info("Application name: " + applicationConfig.applicationName)
        log.info("consumer id: " + NavHttpHeaders.NAV_CONSUMER_ID.asString())

        return HttpHeaders().apply {
           // add(HttpHeader.AUTHORIZATION.asString(), InnloggingUtils.generateBearerTokenForLoggedInUser())
            add(tpsInnsynConfig.bruker, tpsInnsynConfig.passord)
            add(NavHttpHeaders.NAV_PERSONIDENT.asString(), InnloggingUtils.hentFnrFraToken())
            add(NavHttpHeaders.NAV_CONSUMER_ID.asString(), applicationConfig.applicationName)

        }
    }

}
