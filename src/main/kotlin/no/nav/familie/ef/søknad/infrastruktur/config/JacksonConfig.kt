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
        converters.removeIf { it is MappingJackson2HttpMessageConverter }
        converters.add(0, jackson2Converter)
    }
}
