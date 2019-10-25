package no.nav.familie.ef.søknad.config

import no.nav.familie.ef.søknad.util.URIUtil
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.web.util.DefaultUriBuilderFactory
import java.net.URI

@ConfigurationProperties("familie.ef.mottak")
@ConstructorBinding
internal data class MottakConfig(val uri: URI,
                                 val brukernavn: String,
                                 val passord: String) {

    val sendInnUri: URI get() = URIUtil.uri(uri, PATH_SEND_INN)

    val pingUri: URI get() = URIUtil.uri(uri, PATH_PING)

    companion object {
        private const val PATH_SEND_INN = "api/soknad/sendInn"
        private const val PATH_PING = "internal/status/isAlive"
    }

}
