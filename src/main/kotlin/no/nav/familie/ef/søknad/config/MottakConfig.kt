package no.nav.familie.ef.søknad.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.util.DefaultUriBuilderFactory
import java.net.URI

// TODO Endres til ConfigurationProperties ved release av SpringBoot 2.2.0
//@ConfigurationProperties(prefix = "søknad", ignoreUnknownFields = false)
@Component
internal data class MottakConfig(@Value("\${familie.ef.mottak.url}") val url: String,
                                 @Value("\${FAMILIE_EF_MOTTAK_USERNAME}") val username: String,
                                 @Value("\${FAMILIE_EF_MOTTAK_PASSWORD}") val password: String) {

    val sendInnUri: URI get() = DefaultUriBuilderFactory().uriString(url).path(PATH_SEND_INN).build()

    val pingUri: URI get() = DefaultUriBuilderFactory().uriString(url).path(PATH_PING).build()

    companion object {
        private const val PATH_SEND_INN = "soknad/sendInn"
        private const val PATH_PING = "ping"
    }

}
