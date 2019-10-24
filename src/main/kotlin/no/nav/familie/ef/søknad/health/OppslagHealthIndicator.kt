package no.nav.familie.ef.søknad.health

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Metrics
import no.nav.familie.ef.søknad.config.HealthIndicatorConfig
import no.nav.familie.ef.søknad.integration.TpsInnsynServiceClient
import org.springframework.stereotype.Component

@Component
internal class OppslagHealthIndicator(tpsInnsynServiceClient: TpsInnsynServiceClient,
                                      healthIndicatorConfig: HealthIndicatorConfig)
    : AbstractHealthIndicator(tpsInnsynServiceClient, healthIndicatorConfig) {

    override val successCounter: Counter = Metrics.counter("familie.ef.soknad.tps.innsyn.health", "response", "success")
    override val failureCounter: Counter = Metrics.counter("familie.ef.soknad.tps.innsyn.health", "response", "failure")
}
