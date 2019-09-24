import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class WebConfig : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        val corsRegistration = registry.addMapping("/**")
        corsRegistration
                .allowedOrigins("https://familie-ef-soknad.nais.oera-q.local")
                .allowCredentials(true)
    }


}