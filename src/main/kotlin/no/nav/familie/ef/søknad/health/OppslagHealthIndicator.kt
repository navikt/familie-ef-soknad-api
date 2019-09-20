package no.nav.familie.ef.søknad.health

import no.nav.familie.ef.søknad.config.HealthIndicatorConfig
import no.nav.familie.ef.søknad.integration.OppslagClient
import org.springframework.stereotype.Component

@Component
internal class OppslagHealthIndicator(oppslagClient: OppslagClient, healthIndicatorConfig: HealthIndicatorConfig)
    : AbstractHealthIndicator(oppslagClient, healthIndicatorConfig)
