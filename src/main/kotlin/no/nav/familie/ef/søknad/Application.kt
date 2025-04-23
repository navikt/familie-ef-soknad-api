package no.nav.familie.ef.søknad

import no.nav.familie.ef.søknad.infrastruktur.config.CorsProperties
import no.nav.familie.ef.søknad.infrastruktur.config.FamilieIntegrasjonerConfig
import no.nav.familie.ef.søknad.infrastruktur.config.MottakConfig
import no.nav.familie.ef.søknad.infrastruktur.config.RegelverkConfig
import no.nav.familie.ef.søknad.infrastruktur.config.SaksbehandlingConfig
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties

@ConfigurationPropertiesScan
@SpringBootApplication(scanBasePackages = ["no.nav.familie.ef.søknad"])
@EnableConfigurationProperties(
    MottakConfig::class,
    CorsProperties::class,
    FamilieIntegrasjonerConfig::class,
    RegelverkConfig::class,
    SaksbehandlingConfig::class,
)
@EnableJwtTokenValidation(ignore = ["org.springframework", "org.springdoc"])
class Application

fun main(args: Array<String>) {
    SpringApplicationBuilder(Application::class.java).run(*args)
}
