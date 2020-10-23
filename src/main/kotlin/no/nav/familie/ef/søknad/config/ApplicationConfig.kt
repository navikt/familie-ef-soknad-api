package no.nav.familie.ef.søknad.config

import com.fasterxml.jackson.module.kotlin.KotlinModule
import no.nav.familie.ef.søknad.api.filter.CORSResponseFilter
import no.nav.familie.http.config.RestTemplateSts
import no.nav.familie.http.interceptor.ApiKeyInjectingClientInterceptor
import no.nav.familie.http.interceptor.ConsumerIdClientInterceptor
import no.nav.familie.http.interceptor.MdcValuesPropagatingClientInterceptor
import no.nav.familie.http.sts.StsRestClient
import no.nav.familie.kontrakter.felles.objectMapper
import no.nav.familie.log.filter.LogFilter
import no.nav.familie.sikkerhet.EksternBrukerUtils
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
                                         stsConfig: StsConfig,
                                         integrasjoner: FamilieIntegrasjonerConfig): ClientHttpRequestInterceptor {
        val map =
                mapOf(oppslag.uri to Pair(apiKey, oppslag.passord),
                      mottak.uri to Pair(apiKey, mottak.passord),
                      pdlConfig.pdlUri to Pair(apiKey, pdlConfig.passord),
                      stsConfig.uri to Pair(apiKey, stsConfig.passord),
                      integrasjoner.uri to Pair(apiKey, integrasjoner.passord))
        return ApiKeyInjectingClientInterceptor(map)

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
    fun jwtTokenInjectingInterceptor(): ClientHttpRequestInterceptor {
        return AddJwtTokenInterceptor()
    }

    @Bean("restKlientMedApiKey")
    fun restTemplateMedApiKey(consumerIdClientInterceptor: ConsumerIdClientInterceptor,
                              apiKeyInjectingClientInterceptor: ClientHttpRequestInterceptor,
                              jwtTokenInjectingInterceptor: ClientHttpRequestInterceptor): RestOperations {
        return RestTemplateBuilder()
                .interceptors(consumerIdClientInterceptor,
                              apiKeyInjectingClientInterceptor,
                              jwtTokenInjectingInterceptor,
                              MdcValuesPropagatingClientInterceptor())
                .additionalMessageConverters(MappingJackson2HttpMessageConverter(objectMapper))
                .build()
    }

    // TODO skal bruke STS for å hente barn
    @Bean("stsRestKlientMedApiKey")
    fun stsRestTemplateMedApiKey(consumerIdClientInterceptor: ConsumerIdClientInterceptor,
                                 apiKeyInjectingClientInterceptor: ClientHttpRequestInterceptor): RestOperations {
        return RestTemplateBuilder()
                .interceptors(consumerIdClientInterceptor,
                              apiKeyInjectingClientInterceptor,
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
