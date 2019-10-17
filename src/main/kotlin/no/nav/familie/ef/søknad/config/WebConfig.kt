package no.nav.familie.ef.søknad.config

import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@ConfigurationProperties("cors")
@ConstructorBinding
class WebConfigDev(val allowedOrigins: Array<String>) : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
                .allowedOrigins(*allowedOrigins)
                .allowedMethods("GET", "POST")
                .allowCredentials(true).maxAge(3600)
    }

    companion object {
        private val log = LoggerFactory.getLogger(WebConfigDev::class.java)
    }
}

