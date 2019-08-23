package no.nav.familie.ef.soknad.config

import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.web.client.RestOperations

@SpringBootConfiguration
class ApplicationConfig {

    @Bean
    fun restTemplate(vararg interceptors: ClientHttpRequestInterceptor): RestOperations =
            RestTemplateBuilder().interceptors(*interceptors).build()

    @Bean
    fun kotlinModule(): KotlinModule = KotlinModule()

}
