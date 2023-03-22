package no.nav.familie.ef.søknad.config

import org.springframework.boot.context.properties.ConfigurationProperties

@Suppress("ArrayInDataClass")
@ConfigurationProperties(prefix = "cors")
internal data class CorsProperties(val allowedOrigins: Array<String>)
