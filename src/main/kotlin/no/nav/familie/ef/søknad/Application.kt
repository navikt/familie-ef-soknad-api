package no.nav.familie.ef.søknad

import no.nav.familie.ef.søknad.util.getFileAsString
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
    props["familie.ef.mottak.passord"] = getFileAsString("/secrets/apikey/familie-ef-mottak/x-nav-apiKey")
    props["familie.integrasjoner.passord"] = getFileAsString("/secrets/apikey/familie-integrasjoner/x-nav-apiKey")
    props["pdl.passord"] = getFileAsString("/secrets/apikey/pdl-api/x-nav-apiKey")
    props["STS_APIKEY"] = getFileAsString("/secrets/apikey/security-token-service-token/x-nav-apiKey")
    SpringApplicationBuilder(Application::class.java).properties(props).run(*args)
}