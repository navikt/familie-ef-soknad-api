package no.nav.familie.ef.søknad.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.web.util.DefaultUriBuilderFactory
import java.net.URI

@ConfigurationProperties(prefix = "familie.ef.mottak")
@ConstructorBinding
internal data class MottakConfig(val url: String,
                                 val username: String,
                                 val password: String) {

    val sendInnUri: URI get() = DefaultUriBuilderFactory().uriString(url).path(PATH_SEND_INN).build()

    val pingUri: URI get() = DefaultUriBuilderFactory().uriString(url).path(PATH_PING).build()

    companion object {
        private const val PATH_SEND_INN = "soknad/sendInn"
        private const val PATH_PING = "ping"
    }

}
