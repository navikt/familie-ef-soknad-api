package no.nav.familie.ef.søknad.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("regelverk")
@ConstructorBinding
data class RegelverkConfig(val alder: Alder) {

    @ConstructorBinding
    data class Alder(val maks: Int)
}
