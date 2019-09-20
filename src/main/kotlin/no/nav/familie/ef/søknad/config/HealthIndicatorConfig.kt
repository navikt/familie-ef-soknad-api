package no.nav.familie.ef.søknad.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
internal data class HealthIndicatorConfig(@Value("\${familie.ef.heath.indicator.detailed}") val detailed: Boolean)
