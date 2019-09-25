package no.nav.familie.ef.søknad.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

import org.springframework.context.annotation.Profile


@Configuration
@Profile("dev")
class WebConfigDev :  WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST")
                        .allowCredentials(true).maxAge(3600)
            }
        }

@Configuration
@Profile("!dev")
class WebConfig :  WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/api/**")
                .allowedOrigins("https://familie-ef-soknad.nais.oera-q.local")
                .allowedMethods("GET", "POST")
                .allowCredentials(true).maxAge(3600)
    }
}


