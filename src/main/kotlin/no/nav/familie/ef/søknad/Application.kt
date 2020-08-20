package no.nav.familie.ef.søknad

import no.nav.security.token.support.spring.api.EnableJwtTokenValidation
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import java.util.*

@ConfigurationPropertiesScan
@SpringBootApplication(scanBasePackages = ["no.nav.familie.ef.søknad"])
@EnableJwtTokenValidation(ignore = ["org.springframework", "springfox"])
class Application

fun main(args: Array<String>) {
    val props = Properties()
    SpringApplicationBuilder(Application::class.java).properties(props).run(*args)
}