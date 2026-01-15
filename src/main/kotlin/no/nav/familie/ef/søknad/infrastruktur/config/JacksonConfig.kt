package no.nav.familie.ef.søknad.infrastruktur.config

import no.nav.familie.kontrakter.felles.objectMapper
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class JacksonConfig : WebMvcConfigurer {
    override fun extendMessageConverters(
        converters: MutableList<org.springframework.http.converter.HttpMessageConverter<*>>,
    ) {
        val jackson2Converter = MappingJackson2HttpMessageConverter(objectMapper)

        // Fjern evt. eksisterende Jackson2-converters for å unngå duplikater
        converters.removeIf { it is MappingJackson2HttpMessageConverter }

        // Legg vår først
        converters.add(0, jackson2Converter)
    }
}
