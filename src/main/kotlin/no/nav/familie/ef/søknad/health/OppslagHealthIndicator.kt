package no.nav.familie.ef.søknad.health

import no.nav.familie.ef.søknad.config.HealthIndicatorConfig
import no.nav.familie.ef.søknad.integration.TpsInnsynServiceClient
import org.springframework.stereotype.Component

@Component
internal class OppslagHealthIndicator(tpsInnsynServiceClient: TpsInnsynServiceClient,
                                      healthIndicatorConfig: HealthIndicatorConfig)
    : AbstractHealthIndicator(tpsInnsynServiceClient, healthIndicatorConfig)
