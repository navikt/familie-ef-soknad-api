package no.nav.familie.ef.søknad

import no.nav.familie.ef.søknad.util.getFileAsString
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.Properties

@SpringBootApplication(scanBasePackages = ["no.nav.familie.ef.søknad"])
@EnableSwagger2
@EnableJwtTokenValidation(ignore = ["org.springframework", "springfox.documentation.swagger.web.ApiResourceController"])
class Application

fun main(args: Array<String>) {

    val props = Properties()
    /*
    props.put("familie.ef.mottak.passord", getFileAsString("/secrets/apikey/familie-integrasjoner"))
    props.put("familie.integrasjoner.passord", getFileAsString("/secrets/apikey/familie-ef-mottak"))
    props.put("tps.innsyn.passord", getFileAsString("/secrets/apikey/tps-innsyn"))
     */
    SpringApplicationBuilder(Application::class.java).properties(props).run(*args)
}