package no.nav.familie.ef.søknad.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component


@Component
@ConfigurationProperties(prefix = "cors")
class CorsProperties {

    lateinit var allowedOrigins: Array<String>

}
