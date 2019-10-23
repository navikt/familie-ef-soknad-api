package no.nav.familie.ef.søknad.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.util.DefaultUriBuilderFactory

@Component
internal data class TpsInnsynConfig(@Value("\${familie.ef.soknad.tps.innsyn.url}") val url: String,
                                    @Value("\${familie.ef.soknad.tps.innsyn.bruker}") val bruker: String,
                                    @Value("\${familie.ef.soknad.tps.innsyn.passord}") val passord: String) {


    internal val pingUri get() = DefaultUriBuilderFactory().uriString(url).path(PING).build()

    internal val personUri get() = DefaultUriBuilderFactory().uriString(url).path(PERSON).build()

    internal val barnUri get() = DefaultUriBuilderFactory().uriString(url).path(BARN).build()

    companion object {
        private const val PING = "internal/alive"
        private const val PERSON = "person"
        private const val BARN = "barn"
    }


}