package no.nav.familie.ef.søknad.integration

import no.nav.familie.ef.søknad.config.MottakConfig
import no.nav.familie.ef.søknad.integration.dto.KvitteringDto
import no.nav.familie.kontrakter.ef.søknad.Søknad
import org.springframework.stereotype.Service
import org.springframework.web.client.RestOperations


@Service
internal class SøknadClient(operations: RestOperations,
                            private val config: MottakConfig) : PingableRestClient(operations, config.pingUri) {

    fun sendInn(søknad: Søknad): KvitteringDto {
        return postForEntity(config.sendInnUri, søknad)
    }

}
