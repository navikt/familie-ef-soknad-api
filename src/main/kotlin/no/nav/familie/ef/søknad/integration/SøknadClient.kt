package no.nav.familie.ef.søknad.integration

import com.fasterxml.jackson.databind.ObjectMapper
import no.nav.familie.ef.søknad.config.MottakConfig
import no.nav.familie.ef.søknad.integration.dto.KvitteringDto
import no.nav.familie.ef.søknad.integration.dto.SøknadDto
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestOperations


@Service
internal class SøknadClient(operations: RestOperations,
                            private val config: MottakConfig) : PingableRestClient(operations, config.pingUri) {

    fun sendInn(søknadDto: SøknadDto): KvitteringDto {

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.set(config.username, config.password)
        val body = ObjectMapper().writeValueAsString(søknadDto)
        val entity = HttpEntity(body, headers)

        return postForEntity(config.sendInnUri, entity)
    }

}
