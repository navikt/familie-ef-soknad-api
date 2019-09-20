package no.nav.familie.ef.søknad.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.util.DefaultUriBuilderFactory
import java.net.URI

@Configuration
internal data class VirusScanConfig(@Value("\${familie.ef.virus.enabled}") val enabled: Boolean,
                                    @Value("\${familie.ef.virus.url}")val url: String) {

    val uri: URI get() = DefaultUriBuilderFactory().uriString(url).build()

}
