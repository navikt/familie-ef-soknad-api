package no.nav.familie.ef.søknad.health

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Metrics
import no.nav.familie.ef.søknad.config.HealthIndicatorConfig
import no.nav.familie.ef.søknad.integration.SøknadClient
import org.springframework.stereotype.Component

@Component
internal class SøknadHealthIndicator(søknadClient: SøknadClient, healthIndicatorConfig: HealthIndicatorConfig)
    : AbstractHealthIndicator(søknadClient, healthIndicatorConfig) {

    override val successCounter: Counter = Metrics.counter("familie.ef.mottak.health", "response", "success")
    override val failureCounter: Counter = Metrics.counter("familie.ef.mottak.health", "response", "failure")
}
