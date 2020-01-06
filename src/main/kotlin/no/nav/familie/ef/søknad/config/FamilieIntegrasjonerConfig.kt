package no.nav.familie.ef.søknad.config

import no.nav.familie.ef.søknad.util.URIUtil
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.net.URI

@ConfigurationProperties("familie.integrasjoner")
@ConstructorBinding
internal data class FamilieIntegrasjonerConfig(val uri: URI) {

    internal val poststedUri = URIUtil.uri(uri, POSTSTED)

    companion object {
        private const val POSTSTED = "kodeverk/poststed"
    }

}