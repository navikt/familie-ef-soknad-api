package no.nav.familie.ef.søknad.virusscan

import no.nav.familie.ef.søknad.mellomlagring.Vedlegg
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
internal class ClamAvVirusScanner(private val connection: VirusScanConnection) : VirusScanner {

    override fun scan(vedlegg: Vedlegg): Boolean {
        if (connection.isEnabled) {
            return connection.scan(vedlegg)
        }
        LOG.info("Virus-scanning er ikke aktivert")
        return true
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(ClamAvVirusScanner::class.java)
    }
}
