package no.nav.familie.ef.søknad.infrastruktur.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import no.nav.familie.ef.søknad.infrastruktur.sikkerhet.CORSResponseFilter
import no.nav.familie.http.client.RetryOAuth2HttpClient
import no.nav.familie.http.interceptor.BearerTokenClientCredentialsClientInterceptor
import no.nav.familie.http.interceptor.BearerTokenExchangeClientInterceptor
import no.nav.familie.http.interceptor.ConsumerIdClientInterceptor
import no.nav.familie.http.interceptor.MdcValuesPropagatingClientInterceptor
import no.nav.familie.kontrakter.felles.objectMapper
import no.nav.familie.log.NavSystemtype
import no.nav.familie.log.filter.LogFilter
import no.nav.familie.log.filter.RequestTimeFilter
import no.nav.security.token.support.client.core.http.OAuth2HttpClient
import no.nav.security.token.support.client.spring.oauth2.EnableOAuth2Client
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.restclient.RestTemplateBuilder
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestOperations
import org.springframework.web.client.RestTemplate
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
@ComponentScan(basePackages = ["no.nav.familie.unleash"])
internal class ApplicationConfig {
    private val logger = LoggerFactory.getLogger(ApplicationConfig::class.java)

    @Bean
    fun kotlinModule(): KotlinModule = KotlinModule.Builder().build()

    @Bean
    fun corsFilter(corsProperties: CorsProperties): FilterRegistrationBean<CORSResponseFilter> =
        FilterRegistrationBean(CORSResponseFilter(corsProperties)).apply {
            logger.info("Registrerer CORSResponseFilter")
            order = 0
        }

    @Bean
    fun logFilter(): FilterRegistrationBean<LogFilter> =
        FilterRegistrationBean(LogFilter(systemtype = NavSystemtype.NAV_EKSTERN_BRUKERFLATE)).apply {
            logger.info("Registering LogFilter filter")
            order = 1
        }

    @Bean
    fun requestTimeFilter(): FilterRegistrationBean<RequestTimeFilter> =
        FilterRegistrationBean(RequestTimeFilter()).apply {
            logger.info("Registering RequestTimeFilter filter")
            order = 2
        }

    @Bean("tokenExchange")
    fun restTemplate(
        bearerTokenExchangeClientInterceptor: BearerTokenExchangeClientInterceptor,
        mdcValuesPropagatingClientInterceptor: MdcValuesPropagatingClientInterceptor,
        consumerIdClientInterceptor: ConsumerIdClientInterceptor,
    ): RestOperations =
        RestTemplateBuilder()
            .connectTimeout(Duration.of(5, ChronoUnit.SECONDS))
            .readTimeout(Duration.of(25, ChronoUnit.SECONDS))
            .additionalMessageConverters(listOf(MappingJackson2HttpMessageConverter(objectMapper)) + RestTemplate().messageConverters)
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
            .connectTimeout(Duration.of(5, ChronoUnit.SECONDS))
            .readTimeout(Duration.of(25, ChronoUnit.SECONDS))
            .additionalMessageConverters(listOf(MappingJackson2HttpMessageConverter(objectMapper)) + RestTemplate().messageConverters)
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
            .connectTimeout(Duration.of(5, ChronoUnit.SECONDS))
            .readTimeout(Duration.of(25, ChronoUnit.SECONDS))
            .additionalMessageConverters(listOf(MappingJackson2HttpMessageConverter(objectMapper)) + RestTemplate().messageConverters)
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
                    .connectTimeout(Duration.of(2, ChronoUnit.SECONDS))
                    .readTimeout(Duration.of(4, ChronoUnit.SECONDS))
                    .additionalMessageConverters(listOf(MappingJackson2HttpMessageConverter(objectMapper)) + RestTemplate().messageConverters)
                    .build(),
            ),
        )
}
