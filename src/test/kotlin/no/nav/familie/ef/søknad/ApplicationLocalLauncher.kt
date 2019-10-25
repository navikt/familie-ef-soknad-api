package no.nav.familie.ef.søknad

import no.nav.familie.ef.søknad.config.ApplicationConfig
import no.nav.familie.ef.søknad.mock.TpsInnsynMockController
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation
import no.nav.security.token.support.test.spring.TokenGeneratorConfiguration
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.context.annotation.Import
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication(scanBasePackages = ["no.nav.familie.ef.søknad"], exclude = [ErrorMvcAutoConfiguration::class])
@EnableSwagger2
@Import(ApplicationConfig::class, TokenGeneratorConfiguration::class, TpsInnsynMockController::class)
@EnableJwtTokenValidation(ignore = ["org.springframework", "springfox.documentation.swagger.web.ApiResourceController"])
class ApplicationLocalLauncher

fun main(args: Array<String>) {
    SpringApplication.run(ApplicationLocalLauncher::class.java, *args)
}


