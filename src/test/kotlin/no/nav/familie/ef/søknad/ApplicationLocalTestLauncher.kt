package no.nav.familie.ef.søknad

import no.nav.familie.ef.søknad.infrastruktur.config.ApplicationConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.webmvc.autoconfigure.error.ErrorMvcAutoConfiguration
import org.springframework.context.annotation.Import

@SpringBootApplication(
    scanBasePackages = ["no.nav.familie.ef.søknad"],
    exclude = [ErrorMvcAutoConfiguration::class],
)
@Import(ApplicationConfig::class)
class ApplicationLocalTestLauncher
