package no.nav.familie.ef.søknad.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "health.indicator")
@ConstructorBinding
internal data class HealthIndicatorConfig(val detailed: Boolean)
