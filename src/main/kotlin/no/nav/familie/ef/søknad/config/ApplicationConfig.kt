package no.nav.familie.ef.søknad.config

import com.fasterxml.jackson.module.kotlin.KotlinModule
import no.nav.familie.ef.søknad.api.filter.RequestTimeFilter
import no.nav.familie.log.filter.LogFilter
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.web.client.RestOperations

@SpringBootConfiguration
class ApplicationConfig {

    private val LOG = LoggerFactory.getLogger(ApplicationConfig::class.java)

    @Bean
    fun restTemplate(vararg interceptors: ClientHttpRequestInterceptor): RestOperations =
            RestTemplateBuilder().interceptors(*interceptors).build()

    @Bean
    fun kotlinModule(): KotlinModule = KotlinModule()


    @Bean
    fun logFilter(): FilterRegistrationBean<LogFilter> {
        LOG.info("Registering LogFilter filter")
        val filterRegistration = FilterRegistrationBean<LogFilter>()
        filterRegistration.setFilter(LogFilter())
        filterRegistration.order = 1
        return filterRegistration
    }

    @Bean
    fun requestTimeFilter(): FilterRegistrationBean<RequestTimeFilter> {
        LOG.info("Registering RequestTimeFilter filter")
        val filterRegistration = FilterRegistrationBean<RequestTimeFilter>()
        filterRegistration.setFilter(RequestTimeFilter())
        filterRegistration.order = 2
        return filterRegistration
    }
}
