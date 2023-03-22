package no.nav.familie.ef.søknad.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "health.indicator")
internal data class HealthIndicatorConfig(val detailed: Boolean)
