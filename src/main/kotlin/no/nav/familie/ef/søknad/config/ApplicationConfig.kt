package no.nav.familie.ef.søknad.config

import com.fasterxml.jackson.module.kotlin.KotlinModule
import no.nav.familie.ef.søknad.api.filter.CORSResponseFilter
import no.nav.familie.http.interceptor.ApiKeyInjectingClientInterceptor
import no.nav.familie.http.interceptor.BearerTokenClientCredentialsClientInterceptor
import no.nav.familie.http.interceptor.BearerTokenExchangeClientInterceptor
import no.nav.familie.http.interceptor.ConsumerIdClientInterceptor
import no.nav.familie.http.interceptor.MdcValuesPropagatingClientInterceptor
import no.nav.familie.log.filter.LogFilter
import no.nav.familie.log.filter.RequestTimeFilter
import no.nav.security.token.support.client.spring.oauth2.EnableOAuth2Client
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.web.client.RestOperations
import java.time.Duration
import java.time.temporal.ChronoUnit

@SpringBootConfiguration
@EnableOAuth2Client(cacheEnabled = true)
@Import(MdcValuesPropagatingClientInterceptor::class,
        ConsumerIdClientInterceptor::class,
        BearerTokenExchangeClientInterceptor::class,
        BearerTokenClientCredentialsClientInterceptor::class)
internal class ApplicationConfig {

    private val logger = LoggerFactory.getLogger(ApplicationConfig::class.java)
    private val apiKey = "x-nav-apiKey"

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

    @Bean
    fun apiKeyInjectingClientInterceptor(mottak: MottakConfig,
                                         pdlConfig: PdlConfig,
                                         integrasjoner: FamilieIntegrasjonerConfig): ApiKeyInjectingClientInterceptor {
        val map = mapOf(mottak.uri to Pair(apiKey, mottak.passord),
                        pdlConfig.pdlUri to Pair(apiKey, pdlConfig.passord),
                        integrasjoner.uri to Pair(apiKey, integrasjoner.passord))
        return ApiKeyInjectingClientInterceptor(map)
    }

    @Bean("tokenExchange")
    fun restTemplate(bearerTokenExchangeClientInterceptor: BearerTokenExchangeClientInterceptor,
                     mdcValuesPropagatingClientInterceptor: MdcValuesPropagatingClientInterceptor,
                     consumerIdClientInterceptor: ConsumerIdClientInterceptor,
                     apiKeyInjectingClientInterceptor: ApiKeyInjectingClientInterceptor): RestOperations {
        return RestTemplateBuilder()
                .setConnectTimeout(Duration.of(5, ChronoUnit.SECONDS))
                .setReadTimeout(Duration.of(25, ChronoUnit.SECONDS))
                .interceptors(bearerTokenExchangeClientInterceptor,
                              mdcValuesPropagatingClientInterceptor,
                              consumerIdClientInterceptor,
                              apiKeyInjectingClientInterceptor)
                .build()
    }

    @Bean("clientCredential")
    fun clientCredentialRestTemplateMedApiKey(consumerIdClientInterceptor: ConsumerIdClientInterceptor,
                                              bearerTokenClientCredentialsClientInterceptor: BearerTokenClientCredentialsClientInterceptor,
                                              apiKeyInjectingClientInterceptor: ClientHttpRequestInterceptor,
                                              mdcValuesPropagatingClientInterceptor: MdcValuesPropagatingClientInterceptor): RestOperations {
        return RestTemplateBuilder()
                .setConnectTimeout(Duration.of(5, ChronoUnit.SECONDS))
                .setReadTimeout(Duration.of(25, ChronoUnit.SECONDS))
                .interceptors(consumerIdClientInterceptor,
                              apiKeyInjectingClientInterceptor,
                              bearerTokenClientCredentialsClientInterceptor,
                              mdcValuesPropagatingClientInterceptor)
                .build()
    }

    @Bean("utenAuth")
    fun restTemplate(restTemplateBuilder: RestTemplateBuilder,
                     consumerIdClientInterceptor: ConsumerIdClientInterceptor): RestOperations {
        return restTemplateBuilder.additionalInterceptors(consumerIdClientInterceptor,
                                                          MdcValuesPropagatingClientInterceptor()).build()
    }

}
