package no.nav.familie.ef.søknad.health

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Metrics
import no.nav.familie.ef.søknad.config.HealthIndicatorConfig
import no.nav.familie.ef.søknad.integration.FamilieIntegrasjonerClient
import org.springframework.stereotype.Component

@Component
internal class FamilieIntegrasjonHealthIndicator(familieIntegrasjonerClient: FamilieIntegrasjonerClient, healthIndicatorConfig: HealthIndicatorConfig)
    : AbstractHealthIndicator(familieIntegrasjonerClient, healthIndicatorConfig) {

    override val successCounter: Counter = Metrics.counter("familie.integrasjoner.health", "response", "success")
    override val failureCounter: Counter = Metrics.counter("familie.integrasjoner.health", "response", "failure")
}
