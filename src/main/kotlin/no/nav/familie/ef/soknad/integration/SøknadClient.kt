package no.nav.familie.ef.soknad.integration

import no.nav.familie.ef.soknad.config.SøknadConfig
import no.nav.familie.ef.soknad.integration.dto.Kvittering
import no.nav.familie.ef.soknad.integration.dto.Søknad
import org.springframework.stereotype.Service
import org.springframework.web.client.RestOperations

@Service
class SøknadClient(operations: RestOperations,
                   val søknadConfig: SøknadConfig) : AbstractRestClient(operations) {

    override val isEnabled: Boolean = true

    fun sendInn(søknad: Søknad): Kvittering? {

        return Kvittering("""bekreftelse på innsending av "${søknad.text}" """)
//        return postForObject(søknadConfig.Uri, søknad)
    }

}