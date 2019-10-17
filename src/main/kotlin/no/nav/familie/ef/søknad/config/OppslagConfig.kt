package no.nav.familie.ef.søknad.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.web.util.DefaultUriBuilderFactory
import java.net.URI

@ConfigurationProperties("familie.ef.oppslag")
@ConstructorBinding
data class OppslagConfig(val url: String,
                         val key: String) {

    internal val pingUri = DefaultUriBuilderFactory().uriString(url).path(PING).build()

    internal val personURI get() = DefaultUriBuilderFactory().uriString(url).path(PERSON).build()

    internal val søkerinfoURI get() = DefaultUriBuilderFactory().uriString(url).path(SØKERINFO).build()

    internal fun buildAktørIdURI(fnr: String): URI {
        return DefaultUriBuilderFactory().uriString(url).path(AKTØRFNR).queryParam(FNR, fnr).build()
    }

    companion object {
        private const val FNR = "fnr"
        private const val PING = "oppslag/ping"
        private const val PERSON = "person"
        private const val SØKERINFO = "oppslag"
        private const val AKTØRFNR = "oppslag/aktorfnr"
    }


}
