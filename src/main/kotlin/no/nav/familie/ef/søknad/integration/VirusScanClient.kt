package no.nav.familie.ef.søknad.integration

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Metrics
import no.nav.familie.ef.søknad.config.VirusScanConfig
import no.nav.familie.ef.søknad.integration.dto.Result
import no.nav.familie.ef.søknad.integration.dto.ScanResult
import no.nav.familie.ef.søknad.service.mellomlagring.Vedlegg
import no.nav.familie.ef.søknad.util.EnvUtil
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations

@Component
internal class VirusScanClient(operations: RestOperations,
                               private val config: VirusScanConfig,
                               private val env: Environment): AbstractRestClient(operations) {

    val isEnabled = config.enabled

    fun scan(vedlegg: Vedlegg): Boolean {
        try {
            if (EnvUtil.isDevOrLocal(env) && vedlegg.filename.startsWith("virustest")) {
                return false
            }
            log.info("Scanner {}", vedlegg)
            val scanResults: Array<ScanResult> = putForObject(config.uri, vedlegg.bytes)
            if (scanResults.size != 1) {
                log.warn("Uventet respons med lengde {}, forventet lengde er 1", scanResults.size)
                return true
            }
            val scanResult = scanResults[0]
            log.info("Fikk scan result {}", scanResult)
            if (Result.OK == scanResult.result) {
                log.info("Ingen virus i {}", vedlegg.uri())
                INGEN_VIRUS_COUNTER.increment()
                return true
            }
            log.warn("Fant virus i {}, status {}", vedlegg.uri(), scanResult.result)
            VIRUS_COUNTER.increment()
            return false
        } catch (e: Exception) {
            log.warn("Kunne ikke scanne {}", vedlegg, e)
            return true
        }

    }

    companion object {

        private val INGEN_VIRUS_COUNTER =
                counter(Result.OK)
        private val VIRUS_COUNTER =
                counter(Result.FOUND)

        private fun counter(type: Result): Counter {
            return Metrics.counter("virus", "result", type.name)
        }
    }

}
