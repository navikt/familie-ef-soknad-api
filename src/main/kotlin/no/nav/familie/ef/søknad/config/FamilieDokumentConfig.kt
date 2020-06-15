package no.nav.familie.ef.søknad.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@ConfigurationProperties("familie.dokument")
@ConstructorBinding
internal data class FamilieDokumentConfig(val uri: URI) {

    internal val hentVedleggUri = UriComponentsBuilder.fromUri(uri).path(HENT).build().toUri()

    internal val pingUri = UriComponentsBuilder.fromUri(uri).path(PING).build().toUri()

    companion object {
        private const val HENT = "/mapper/familievedlegg/"
        private const val PING = "/mapper/ping"
    }

}
