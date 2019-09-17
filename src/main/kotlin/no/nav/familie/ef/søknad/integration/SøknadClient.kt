package no.nav.familie.ef.søknad.integration

import com.fasterxml.jackson.databind.ObjectMapper
import no.nav.familie.ef.søknad.config.MottakConfig
import no.nav.familie.ef.søknad.integration.dto.KvitteringDto
import no.nav.familie.ef.søknad.integration.dto.SøknadDto
import org.springframework.stereotype.Service
import org.springframework.web.client.RestOperations

@Service
internal class SøknadClient(operations: RestOperations,
                   private val config: MottakConfig) : PingableRestClient(operations, config.pingUri) {

    fun sendInn(søknadDto: SøknadDto): KvitteringDto {
        return postForEntity(config.sendInnUri, ObjectMapper().writeValueAsString(søknadDto))
    }

}