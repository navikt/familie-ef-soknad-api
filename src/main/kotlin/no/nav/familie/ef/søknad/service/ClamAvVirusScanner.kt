package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.integration.VirusScanClient
import no.nav.familie.ef.søknad.service.mellomlagring.Vedlegg
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
internal class ClamAvVirusScanner(private val client: VirusScanClient) : VirusScanner {

    override fun scan(vedlegg: Vedlegg): Boolean {
        if (client.isEnabled) {
            return client.scan(vedlegg)
        }
        LOG.info("Virus-scanning er ikke aktivert")
        return true
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(ClamAvVirusScanner::class.java)
    }
}
