package no.nav.familie.ef.søknad.config

import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.common.collect.ImmutableMap
import no.nav.familie.ef.søknad.api.filter.CORSResponseFilter
import no.nav.familie.ef.søknad.api.filter.RequestTimeFilter
import no.nav.familie.ef.søknad.interceptor.ApiKeyInjectingClientInterceptor
import no.nav.familie.log.filter.LogFilter
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.web.client.RestOperations
import java.net.URI

@SpringBootConfiguration
internal class ApplicationConfig(@Value("\${application.name}") val applicationName: String) {

    private val logger = LoggerFactory.getLogger(ApplicationConfig::class.java)

    @Bean
    fun apiKeyInjectingClientInterceptor(oppslag: TpsInnsynConfig, mottak: MottakConfig, integrasjoner: FamilieIntegrasjonerConfig): ClientHttpRequestInterceptor {
        val builder = ImmutableMap.builder<URI, Pair<String, String>>()
        builder.put(oppslag.uri, Pair(oppslag.brukernavn, oppslag.passord))
        builder.put(mottak.uri, Pair(mottak.brukernavn, mottak.passord))
        builder.put(integrasjoner.uri, Pair(integrasjoner.brukernavn, integrasjoner.passord))
        return ApiKeyInjectingClientInterceptor(builder.build())

    }

    @Bean
    fun restTemplate(vararg interceptors: ClientHttpRequestInterceptor): RestOperations {
        logger.info("Registrerer interceptors {}", interceptors.contentToString())
        return RestTemplateBuilder()
                .interceptors(*interceptors)
                .build()
    }

    @Bean
    fun kotlinModule(): KotlinModule = KotlinModule()

    @Bean
    fun corsFilter(corsProperties: CorsProperties): FilterRegistrationBean<CORSResponseFilter> {
        logger.info("Registrerer CORSResponseFilter")
        val filterRegistration = FilterRegistrationBean<CORSResponseFilter>()
        filterRegistration.filter = CORSResponseFilter(corsProperties)
        filterRegistration.order = 0
        return filterRegistration
    }

    @Bean
    fun logFilter(): FilterRegistrationBean<LogFilter> {
        logger.info("Registering LogFilter filter")
        val filterRegistration = FilterRegistrationBean<LogFilter>()
        filterRegistration.filter = LogFilter()
        filterRegistration.order = 1
        return filterRegistration
    }

    @Bean
    fun requestTimeFilter(): FilterRegistrationBean<RequestTimeFilter> {
        logger.info("Registering RequestTimeFilter filter")
        val filterRegistration = FilterRegistrationBean<RequestTimeFilter>()
        filterRegistration.filter = RequestTimeFilter()
        filterRegistration.order = 2
        return filterRegistration
    }
}
