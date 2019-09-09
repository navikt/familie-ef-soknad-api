package no.nav.familie.ef.søknad.config

import com.fasterxml.jackson.module.kotlin.KotlinModule
import no.nav.familie.ef.soknad.featuretoggle.UnleashService
import no.nav.familie.ef.soknad.featuretoggle.UnleashServiceImpl
import org.springframework.beans.factory.annotation.Value
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


    @Value("\${UNLEASH_API_URL:}")
    lateinit var unleashApiUrl : String

    @Value("\${environment.name:}")
    lateinit var miljoNavn : String

    @Bean
    fun featureToggle(): UnleashService = UnleashServiceImpl.nyFraApiUrl(unleashApiUrl)

}
