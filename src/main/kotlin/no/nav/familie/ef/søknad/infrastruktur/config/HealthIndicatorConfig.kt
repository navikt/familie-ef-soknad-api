package no.nav.familie.ef.søknad.infrastruktur.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "health.indicator")
internal data class HealthIndicatorConfig(
    val detailed: Boolean,
)
