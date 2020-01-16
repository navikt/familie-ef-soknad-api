package no.nav.familie.ef.søknad.config

import no.nav.familie.ef.søknad.util.URIUtil
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.net.URI

@ConfigurationProperties("familie.integrasjoner")
@ConstructorBinding
internal data class FamilieIntegrasjonerConfig(val uri: URI,
                                               val passord: String) {

    internal val poststedUri = URIUtil.uri(uri, POSTSTED)

    val pingUri: URI get() = URIUtil.uri(uri, PING)

    companion object {
        private const val POSTSTED = "kodeverk/poststed"
        private const val PING = "internal/health"
    }

}