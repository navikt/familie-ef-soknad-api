package no.nav.familie.ef.søknad.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.net.URI

@ConfigurationProperties(prefix = "virus")
@ConstructorBinding
internal data class VirusScanConfig(val enabled: Boolean,
                                    val uri: URI)
