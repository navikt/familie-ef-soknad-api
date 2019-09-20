package no.nav.familie.ef.søknad.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.util.DefaultUriBuilderFactory
import java.net.URI

@Component
data class OppslagConfig(@Value("\${familie.ef.oppslag.url}") val url: String,
                         @Value("\${familie.ef.oppslag.key}") val key: String) {

    internal val pingUri get() = DefaultUriBuilderFactory().uriString(url).path(PING).build()

    internal val personURI get() = DefaultUriBuilderFactory().uriString(url).path(PERSON).build()

    internal val søkerinfoURI get() = DefaultUriBuilderFactory().uriString(url).path(SØKERINFO).build()

    internal fun buildAktørIdURI(fnr: String) : URI {
        return DefaultUriBuilderFactory().uriString(url).path(AKTØRFNR).queryParam(
                FNR, fnr).build()
    }

    companion object {
        private const val FNR = "fnr"
        private const val PING = "oppslag/ping"
        private const val PERSON = "person"
        private const val SØKERINFO = "oppslag"
        private const val AKTØRFNR = "oppslag/aktorfnr"
    }


}
