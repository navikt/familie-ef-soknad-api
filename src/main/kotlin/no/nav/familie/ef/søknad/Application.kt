package no.nav.familie.ef.søknad

import no.nav.security.spring.oidc.api.EnableOIDCTokenValidation
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication(scanBasePackages = ["no.nav.familie.ef.søknad"])
@EnableSwagger2
@EnableOIDCTokenValidation(ignore = ["springfox.documentation.swagger.web.ApiResourceController"])
class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
