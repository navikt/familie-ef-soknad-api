package no.nav.familie.ef.søknad

import no.nav.familie.ef.søknad.util.getFileAsString
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.*

@SpringBootApplication(scanBasePackages = ["no.nav.familie.ef.søknad"])
@EnableSwagger2
@EnableJwtTokenValidation(ignore = ["org.springframework", "springfox.documentation.swagger.web.ApiResourceController"])
class Application

fun main(args: Array<String>) {



    val props = Properties()

    props.put("familie.ef.mottak.passord", getFileAsString("/secrets/apikey/familie-integrasjoner/x-nav-apiKey"))
    props.put("familie.integrasjoner.passord", getFileAsString("/secrets/apikey/familie-ef-mottak/x-nav-apiKey"))
    props.put("tps.innsyn.passord", getFileAsString("/secrets/apikey/tps-innsyn/x-nav-apiKey"))
    //SpringApplicationBuilder(Application::class.java).properties(props).run(*args)


    //val app = SpringApplication(Application::class.java)
    //app.setDefaultProperties(props)
    //app.run(*args)
    SpringApplication.run(Application::class.java, *args)

}