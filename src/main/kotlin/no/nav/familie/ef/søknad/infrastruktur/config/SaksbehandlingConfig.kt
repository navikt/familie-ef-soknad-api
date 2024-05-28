package no.nav.familie.ef.søknad.infrastruktur.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@ConfigurationProperties("familie.ef.saksbehandling")
data class SaksbehandlingConfig(val uri: URI) {
    internal val hentStønadsperioderUri = byggUri(PATH_HENT_STØNADSPERIODER)
    internal val pingUri = byggUri(PATH_PING)

    private fun byggUri(path: String) = UriComponentsBuilder.fromUri(uri).path(path).build().toUri()

    companion object {
        private const val PATH_HENT_STØNADSPERIODER = "/ekstern/minside/stonadsperioder"
        private const val PATH_PING = "ping"
    }
}
