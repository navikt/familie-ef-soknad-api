package no.nav.familie.ef.søknad.config

import no.nav.familie.ef.søknad.util.URIUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.web.util.DefaultUriBuilderFactory
import java.net.URI

@ConfigurationProperties("tps.innsyn")
@ConstructorBinding
internal data class TpsInnsynConfig(val uri: URI,
                                    val brukernavn: String,
                                    val passord: String) {

    internal val personUri get() = URIUtil.uri(uri, PERSON)

    internal val barnUri get() = URIUtil.uri(uri, BARN)

    companion object {
        private const val PERSON = "person"
        private const val BARN = "barn"
    }

}