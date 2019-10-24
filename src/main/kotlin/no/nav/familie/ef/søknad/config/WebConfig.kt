package no.nav.familie.ef.søknad.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@ConfigurationProperties("cors")
@ConstructorBinding
class WebConfig(val allowedOrigins: Array<String>) : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
                .allowedOrigins(*allowedOrigins)
                .allowedMethods("GET", "POST")
                .allowCredentials(true)
                .maxAge(3600)
    }

}

