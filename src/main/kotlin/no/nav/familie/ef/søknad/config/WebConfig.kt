package no.nav.familie.ef.søknad.config

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebConfigDev(private val corsProperties: CorsProperties) : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/api/**")
                .allowedOrigins(*corsProperties.allowedOrigins)
                .allowedMethods("GET", "POST")
                .allowCredentials(true).maxAge(3600)
    }

    companion object {
        private val log = LoggerFactory.getLogger(WebConfigDev::class.java)
    }
}

