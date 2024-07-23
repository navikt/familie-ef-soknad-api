package no.nav.familie.ef.søknad.infrastruktur.config

import com.fasterxml.jackson.module.kotlin.KotlinModule
import no.nav.familie.ef.søknad.infrastruktur.sikkerhet.CORSResponseFilter
import no.nav.familie.http.client.RetryOAuth2HttpClient
import no.nav.familie.http.interceptor.BearerTokenClientCredentialsClientInterceptor
import no.nav.familie.http.interceptor.BearerTokenExchangeClientInterceptor
import no.nav.familie.http.interceptor.ConsumerIdClientInterceptor
import no.nav.familie.http.interceptor.MdcValuesPropagatingClientInterceptor
import no.nav.familie.log.filter.LogFilter
import no.nav.familie.log.filter.RequestTimeFilter
import no.nav.security.token.support.client.core.http.OAuth2HttpClient
import no.nav.security.token.support.client.spring.oauth2.EnableOAuth2Client
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestOperations
import java.time.Duration
import java.time.temporal.ChronoUnit

@SpringBootConfiguration
@EnableOAuth2Client(cacheEnabled = true)
@Import(
    MdcValuesPropagatingClientInterceptor::class,
    ConsumerIdClientInterceptor::class,
    BearerTokenExchangeClientInterceptor::class,
    BearerTokenClientCredentialsClientInterceptor::class,
)
internal class ApplicationConfig {
    private val logger = LoggerFactory.getLogger(ApplicationConfig::class.java)

    @Bean
    fun kotlinModule(): KotlinModule = KotlinModule.Builder().build()

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

    @Bean("tokenExchange")
    fun restTemplate(
        bearerTokenExchangeClientInterceptor: BearerTokenExchangeClientInterceptor,
        mdcValuesPropagatingClientInterceptor: MdcValuesPropagatingClientInterceptor,
        consumerIdClientInterceptor: ConsumerIdClientInterceptor,
    ): RestOperations =
        RestTemplateBuilder()
            .setConnectTimeout(Duration.of(5, ChronoUnit.SECONDS))
            .setReadTimeout(Duration.of(25, ChronoUnit.SECONDS))
            .interceptors(
                bearerTokenExchangeClientInterceptor,
                mdcValuesPropagatingClientInterceptor,
                consumerIdClientInterceptor,
            ).build()

    @Bean("clientCredential")
    fun clientCredentialRestTemplateMedApiKey(
        consumerIdClientInterceptor: ConsumerIdClientInterceptor,
        bearerTokenClientCredentialsClientInterceptor: BearerTokenClientCredentialsClientInterceptor,
        mdcValuesPropagatingClientInterceptor: MdcValuesPropagatingClientInterceptor,
    ): RestOperations =
        RestTemplateBuilder()
            .setConnectTimeout(Duration.of(5, ChronoUnit.SECONDS))
            .setReadTimeout(Duration.of(25, ChronoUnit.SECONDS))
            .interceptors(
                consumerIdClientInterceptor,
                bearerTokenClientCredentialsClientInterceptor,
                mdcValuesPropagatingClientInterceptor,
            ).build()

    @Bean("utenAuth")
    fun restTemplateUtenAuth(
        consumerIdClientInterceptor: ConsumerIdClientInterceptor,
        mdcValuesPropagatingClientInterceptor: MdcValuesPropagatingClientInterceptor,
    ): RestOperations =
        RestTemplateBuilder()
            .setConnectTimeout(Duration.of(5, ChronoUnit.SECONDS))
            .setReadTimeout(Duration.of(25, ChronoUnit.SECONDS))
            .additionalInterceptors(
                consumerIdClientInterceptor,
                mdcValuesPropagatingClientInterceptor,
            ).build()

    @Primary
    @Bean
    fun oAuth2HttpClient(): OAuth2HttpClient =
        RetryOAuth2HttpClient(
            RestClient.create(
                RestTemplateBuilder()
                    .setConnectTimeout(Duration.of(2, ChronoUnit.SECONDS))
                    .setReadTimeout(Duration.of(4, ChronoUnit.SECONDS))
                    .build(),
            ),
        )
}
