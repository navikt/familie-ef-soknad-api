package no.nav.familie.ef.søknad.health

import no.nav.familie.ef.søknad.config.HealthIndicatorConfig
import no.nav.familie.ef.søknad.integration.SøknadClient
import org.springframework.stereotype.Component

@Component
internal class SøknadHealthIndicator(søknadClient: SøknadClient, healthIndicatorConfig: HealthIndicatorConfig)
    : AbstractHealthIndicator(søknadClient, healthIndicatorConfig)
