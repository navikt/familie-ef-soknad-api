package no.nav.familie.ef.søknad.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@ConfigurationProperties("familie.integrasjoner")
@ConstructorBinding
data class FamilieIntegrasjonerConfig(val uri: URI,
                                      val passord: String) {

    internal val poststedUri = UriComponentsBuilder.fromUri(uri).path(POSTSTED).build().toUri()

    internal val pingUri = UriComponentsBuilder.fromUri(uri).path(PING).build().toUri()

    companion object {
        private const val POSTSTED = "/kodeverk/poststed/"
        private const val PING = "internal/health"
    }

}
