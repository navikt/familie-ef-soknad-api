package no.nav.familie.ef.søknad.config

import no.nav.familie.ef.søknad.util.URIUtil
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.net.URI

@ConfigurationProperties("familie.dokument")
@ConstructorBinding
internal data class FamilieDokumentConfig(val uri: URI) {

    internal val hentVedleggUri = URIUtil.uri(uri, HENT)

    val pingUri: URI get() = URIUtil.uri(uri, PING)

    companion object {
        private const val HENT = "mapper/familievedlegg"
        private const val PING = "mapper/ping"
    }

}