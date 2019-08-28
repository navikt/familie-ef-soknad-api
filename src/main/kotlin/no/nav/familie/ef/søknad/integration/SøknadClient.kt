package no.nav.familie.ef.søknad.integration

import no.nav.familie.ef.søknad.config.MottakConfig
import no.nav.familie.ef.søknad.integration.dto.Kvittering
import no.nav.familie.ef.søknad.integration.dto.Søknad
import org.springframework.stereotype.Service
import org.springframework.web.client.RestOperations
import org.springframework.web.util.DefaultUriBuilderFactory

@Service
class SøknadClient(operations: RestOperations,
                   mottakConfig: MottakConfig) : AbstractRestClient(operations) {

    override val isEnabled: Boolean = true

    private val sendInnUri = DefaultUriBuilderFactory().uriString(mottakConfig.uri).path(PATH_SEND_INN).build()

    fun sendInn(søknad: Søknad): Kvittering? {
        return postForObject(sendInnUri, søknad)
    }

    companion object {
        private const val PATH_SEND_INN = "soknad/sendInn"
    }

}