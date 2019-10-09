package no.nav.familie.ef.søknad.integration

import com.fasterxml.jackson.databind.ObjectMapper
import no.nav.familie.ef.søknad.config.MottakConfig
import no.nav.familie.ef.søknad.integration.dto.KvitteringDto
import no.nav.familie.ef.søknad.integration.dto.SøknadDto
import org.springframework.stereotype.Service
import org.springframework.web.client.RestOperations
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import no.nav.familie.http.client.NavHttpHeaders
import no.nav.familie.log.mdc.MDCConstants.*
import org.slf4j.MDC


@Service
internal class SøknadClient(operations: RestOperations,
                   private val config: MottakConfig) : PingableRestClient(operations, config.pingUri) {

    val NAV_CALLID_HEADER = NavHttpHeaders.NAV_CALLID.asString()
    val NAV_CONSUMERID_HEADER = NavHttpHeaders.NAV_CONSUMER_ID.asString()
    val NAV_USERID_HEADER = NavHttpHeaders.NAV_PERSONIDENT.asString()
    fun sendInn(søknadDto: SøknadDto): KvitteringDto {
        log.info(MDC_REQUEST_ID + " "+ MDC.get(MDC_REQUEST_ID) + ", " + MDC_CONSUMER_ID + " "+ MDC.get(MDC_CONSUMER_ID) + ", " + MDC_CALL_ID + " "+ MDC.get(MDC_CALL_ID) + ", " + MDC_USER_ID + " " + MDC.get(MDC_USER_ID) )
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.set(config.username, config.password)
        headers.set(NAV_CONSUMERID_HEADER, MDC.get(MDC_CONSUMER_ID))
        headers.set(NAV_CALLID_HEADER, MDC.get(MDC_CALL_ID))
        headers.set(NAV_USERID_HEADER, MDC.get(MDC_USER_ID))
        val body = ObjectMapper().writeValueAsString(søknadDto)
        val entity = HttpEntity(body, headers)

        return postForEntity(config.sendInnUri, entity)
    }

}
