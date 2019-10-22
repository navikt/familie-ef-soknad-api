package no.nav.familie.ef.søknad.health

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Metrics
import no.nav.familie.ef.søknad.config.HealthIndicatorConfig
import no.nav.familie.ef.søknad.integration.OppslagClient
import org.springframework.stereotype.Component

@Component
internal class OppslagHealthIndicator(oppslagClient: OppslagClient, healthIndicatorConfig: HealthIndicatorConfig)
    : AbstractHealthIndicator(oppslagClient, healthIndicatorConfig) {

    override val successCounter: Counter = Metrics.counter("tps.innsyn.health", "response", "success")
    override val failureCounter: Counter = Metrics.counter("tps.innsyn.health", "response", "failure")
}
