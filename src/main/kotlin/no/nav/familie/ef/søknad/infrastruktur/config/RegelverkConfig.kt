package no.nav.familie.ef.søknad.infrastruktur.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("regelverk")
data class RegelverkConfig(val alder: Alder) {
    data class Alder(val maks: Int)
}
