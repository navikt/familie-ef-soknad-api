package no.nav.familie.ef.søknad.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@ConfigurationProperties("familie.integrasjoner")
@ConstructorBinding
data class FamilieIntegrasjonerConfig(val uri: URI,
                                      val passord: String) {

    val kodeverkLandkoderUri: URI =
            UriComponentsBuilder.fromUri(uri).pathSegment(PATH_KODEVERK_LANDKODER).build().toUri()

    val kodeverkPoststedUri: URI =
            UriComponentsBuilder.fromUri(uri).pathSegment(PATH_KODEVERK_POSTSTED).build().toUri()

    internal val pingUri = UriComponentsBuilder.fromUri(uri).pathSegment(PING).build().toUri()

    companion object {
        private const val PATH_KODEVERK_LANDKODER = "kodeverk/landkoder"
        private const val PATH_KODEVERK_POSTSTED = "kodeverk/poststed"
        private const val PING = "ping"
    }

}
