package no.nav.familie.ef.søknad.integrationTest

import no.nav.familie.ef.søknad.config.ApplicationConfig
import no.nav.security.token.support.spring.api.EnableJwtTokenValidation
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.context.annotation.Import

@SpringBootApplication(
    scanBasePackages = ["no.nav.familie.ef.søknad"],
    exclude = [ErrorMvcAutoConfiguration::class]
)
@Import(ApplicationConfig::class)
@EnableJwtTokenValidation(ignore = ["org.springframework", "org.springdoc"])
class ApplicationLocalTestLauncher
