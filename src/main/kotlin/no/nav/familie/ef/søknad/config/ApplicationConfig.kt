package no.nav.familie.ef.søknad.config

import com.fasterxml.jackson.module.kotlin.KotlinModule
import no.nav.familie.ef.søknad.api.filter.CORSResponseFilter
import no.nav.familie.http.config.RestTemplateSts
import no.nav.familie.http.interceptor.*
import no.nav.familie.http.sts.StsRestClient
import no.nav.familie.kontrakter.felles.objectMapper
import no.nav.familie.log.filter.LogFilter
import no.nav.familie.sikkerhet.EksternBrukerUtils
import no.nav.security.token.support.spring.validation.interceptor.BearerTokenClientHttpRequestInterceptor
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestOperations

@SpringBootConfiguration
@Import(MdcValuesPropagatingClientInterceptor::class,
        ConsumerIdClientInterceptor::class,
        StsRestClient::class,
        RestTemplateSts::class)
internal class ApplicationConfig {

    private val logger = LoggerFactory.getLogger(ApplicationConfig::class.java)
    private val apiKey = "x-nav-apiKey"

    @Bean
    fun apiKeyInjectingClientInterceptor(oppslag: TpsInnsynConfig,
                                         mottak: MottakConfig,
                                         pdlConfig: PdlConfig,
                                         integrasjoner: FamilieIntegrasjonerConfig): ApiKeyInjectingClientInterceptor {
        val map =
                mapOf(oppslag.uri to Pair(apiKey, oppslag.passord),
                      mottak.uri to Pair(apiKey, mottak.passord),
                      pdlConfig.pdlUri to Pair(apiKey, pdlConfig.passord),
                      integrasjoner.uri to Pair(apiKey, integrasjoner.passord))
        return ApiKeyInjectingClientInterceptor(map)
    }

    @Bean("restTemplate")
    fun restTemplate(bearerTokenClientHttpRequestInterceptor: BearerTokenClientHttpRequestInterceptor,
                     mdcValuesPropagatingClientInterceptor: MdcValuesPropagatingClientInterceptor,
                     consumerIdClientInterceptor: ConsumerIdClientInterceptor,
                     apiKeyInjectingClientInterceptor: ApiKeyInjectingClientInterceptor): RestOperations {
        return RestTemplateBuilder()
                .interceptors(bearerTokenClientHttpRequestInterceptor,
                              mdcValuesPropagatingClientInterceptor,
                              consumerIdClientInterceptor,
                              apiKeyInjectingClientInterceptor)
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

    @Bean("restKlientMedApiKey")
    fun restTemplateMedApiKey(consumerIdClientInterceptor: ConsumerIdClientInterceptor,
                              apiKeyInjectingClientInterceptor: ClientHttpRequestInterceptor): RestOperations {
        return RestTemplateBuilder()
                .interceptors(consumerIdClientInterceptor,
                              apiKeyInjectingClientInterceptor,
                              AddJwtTokenInterceptor(),
                              MdcValuesPropagatingClientInterceptor())
                .additionalMessageConverters(MappingJackson2HttpMessageConverter(objectMapper))
                .build()
    }

    @Bean("stsRestKlientMedApiKey")
    fun stsRestTemplateMedApiKey(consumerIdClientInterceptor: ConsumerIdClientInterceptor,
                                 stsBearerTokenClientInterceptor: StsBearerTokenClientInterceptor,
                                 apiKeyInjectingClientInterceptor: ClientHttpRequestInterceptor): RestOperations {
        return RestTemplateBuilder()
                .interceptors(consumerIdClientInterceptor,
                              apiKeyInjectingClientInterceptor,
                              stsBearerTokenClientInterceptor,
                              MdcValuesPropagatingClientInterceptor())
                .additionalMessageConverters(MappingJackson2HttpMessageConverter(objectMapper))
                .build()
    }

    class AddJwtTokenInterceptor : ClientHttpRequestInterceptor {

        override fun intercept(request: HttpRequest, body: ByteArray, execution: ClientHttpRequestExecution): ClientHttpResponse {
            request.headers["Authorization"] = "Bearer ${EksternBrukerUtils.getBearerTokenForLoggedInUser()}"
            return execution.execute(request, body)
        }
    }
}
