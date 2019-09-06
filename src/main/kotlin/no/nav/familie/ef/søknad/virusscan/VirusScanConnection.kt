package no.nav.familie.ef.søknad.virusscan

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Metrics
import no.nav.familie.ef.søknad.config.VirusScanConfig
import no.nav.familie.ef.søknad.integration.AbstractRestClient
import no.nav.familie.ef.søknad.mellomlagring.Vedlegg
import no.nav.familie.ef.søknad.util.EnvUtil
import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations
import org.springframework.web.util.DefaultUriBuilderFactory

@Component
internal class VirusScanConnection(operations: RestOperations,
                                   config: VirusScanConfig,
                                   private val env: Environment): AbstractRestClient(operations) {

    val isEnabled = config.enabled
    private val uri = DefaultUriBuilderFactory().uriString(config.url).build()

    fun scan(vedlegg: Vedlegg): Boolean {
        try {
            if (EnvUtil.isDevOrLocal(env) && vedlegg.filename.startsWith("virustest")) {
                return false
            }
            LOG.info("Scanner {}", vedlegg)
            val scanResults: Array<ScanResult> = putForObject(uri, vedlegg.bytes)
            if (scanResults.size != 1) {
                LOG.warn("Uventet respons med lengde {}, forventet lengde er 1", scanResults.size)
                return true
            }
            val scanResult = scanResults[0]
            LOG.info("Fikk scan result {}", scanResult)
            if (Result.OK == scanResult.result) {
                LOG.info("Ingen virus i {}", vedlegg.uri())
                INGENVIRUS_COUNTER.increment()
                return true
            }
            LOG.warn("Fant virus i {}, status {}", vedlegg.uri(), scanResult.result)
            VIRUS_COUNTER.increment()
            return false
        } catch (e: Exception) {
            LOG.warn("Kunne ikke scanne {}", vedlegg, e)
            return true
        }

    }

    companion object {

        private val LOG = LoggerFactory.getLogger(VirusScanConnection::class.java)
        private val INGENVIRUS_COUNTER = counter(Result.OK)
        private val VIRUS_COUNTER = counter(Result.FOUND)

        private fun counter(type: Result): Counter {
            return Metrics.counter("virus", "result", type.name)
        }
    }

}
