package no.nav.familie.ef.søknad.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("regelverk")
data class RegelverkConfig(val alder: Alder) {

    data class Alder(val maks: Int)
}
