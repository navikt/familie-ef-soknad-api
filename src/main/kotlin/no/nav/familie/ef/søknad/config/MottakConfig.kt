package no.nav.familie.ef.søknad.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@ConfigurationProperties("familie.ef.mottak")
@ConstructorBinding
internal data class MottakConfig(val uri: URI,
                                 val passord: String) {

    internal val sendInnUri = UriComponentsBuilder.fromUri(uri).path(PATH_SEND_INN).build().toUri()

    internal val pingUri = UriComponentsBuilder.fromUri(uri).path(PATH_PING).build().toUri()

    companion object {
        private const val PATH_SEND_INN = "/soknad"
        private const val PATH_PING = "/ping"
    }

}
