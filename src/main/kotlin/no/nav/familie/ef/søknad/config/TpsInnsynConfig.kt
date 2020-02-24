package no.nav.familie.ef.søknad.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@ConfigurationProperties("tps.innsyn")
@ConstructorBinding
internal data class TpsInnsynConfig(val uri: URI,
                                    val passord: String) {

    internal val personUri = UriComponentsBuilder.fromUri(uri).path(PERSON).build().toUri()

    internal val barnUri = UriComponentsBuilder.fromUri(uri).path(BARN).build().toUri()

    companion object {
        private const val PERSON = "person"
        private const val BARN = "barn"
    }

}
