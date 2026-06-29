package no.nav.familie.ef.søknad.infrastruktur.config

import no.nav.familie.ef.søknad.infrastruktur.sikkerhet.CORSResponseFilter
import no.nav.familie.felles.tokenklient.entraid.EntraIDClient
import no.nav.familie.felles.tokenklient.entraid.MaskinTilMaskinTokenInterceptor
import no.nav.familie.felles.tokenklient.tokenx.TokenXClient
import no.nav.familie.felles.tokenklient.tokenx.TokenXInterceptor
import no.nav.familie.log.NavSystemtype
import no.nav.familie.log.filter.LogFilter
import no.nav.familie.log.filter.RequestTimeFilter
import no.nav.familie.log.interceptor.ConsumerIdClientInterceptor
import no.nav.familie.log.interceptor.MdcValuesPropagatingClientInterceptor
import no.nav.familie.sikkerhet.EksternBrukerUtils
import no.nav.familie.sikkerhet.context.FamilieFellesSpringSecurityKonfigurasjon
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.restclient.RestTemplateBuilder
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.http.converter.ByteArrayHttpMessageConverter
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter
import org.springframework.web.client.RestOperations
import org.springframework.web.client.RestTemplate
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.KotlinModule
import java.time.Duration
import java.time.temporal.ChronoUnit
import no.nav.familie.kontrakter.felles.jsonMapper as kontraktJsonMapper

@SpringBootConfiguration
@ComponentScan(
    basePackages = [
        "no.nav.familie.unleash",
        "no.nav.familie.felles.tokenklient",
    ],
)
@Import(
    FamilieFellesSpringSecurityKonfigurasjon::class,
    MdcValuesPropagatingClientInterceptor::class,
    ConsumerIdClientInterceptor::class,
)
internal class ApplicationConfig {
    private val logger = LoggerFactory.getLogger(ApplicationConfig::class.java)

    @Bean
    @Primary
    fun jsonMapper(): JsonMapper = kontraktJsonMapper

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

    @Bean("mottakRestTemplate")
    fun mottakRestTemplate(
        tokenXClient: TokenXClient,
        @Value("\${familie.ef.mottak.audience}") scope: String,
        mdcValuesPropagatingClientInterceptor: MdcValuesPropagatingClientInterceptor,
        consumerIdClientInterceptor: ConsumerIdClientInterceptor,
    ): RestOperations =
        lagTokenXRestTemplate(tokenXClient, scope, mdcValuesPropagatingClientInterceptor, consumerIdClientInterceptor)
            .withByteArrayConverterForPdf()
            .build()

    @Bean("saksbehandlingRestTemplate")
    fun saksbehandlingRestTemplate(
        tokenXClient: TokenXClient,
        @Value("\${familie.ef.saksbehandling.audience}") scope: String,
        mdcValuesPropagatingClientInterceptor: MdcValuesPropagatingClientInterceptor,
        consumerIdClientInterceptor: ConsumerIdClientInterceptor,
    ): RestOperations =
        lagTokenXRestTemplate(tokenXClient, scope, mdcValuesPropagatingClientInterceptor, consumerIdClientInterceptor)
            .build()

    @Bean("pdlRestTemplate")
    fun pdlRestTemplate(
        tokenXClient: TokenXClient,
        @Value("\${PDL_AUDIENCE}") scope: String,
        mdcValuesPropagatingClientInterceptor: MdcValuesPropagatingClientInterceptor,
        consumerIdClientInterceptor: ConsumerIdClientInterceptor,
    ): RestOperations =
        lagTokenXRestTemplate(tokenXClient, scope, mdcValuesPropagatingClientInterceptor, consumerIdClientInterceptor)
            .build()

    @Bean("safRestTemplate")
    fun safRestTemplate(
        tokenXClient: TokenXClient,
        @Value("\${SAF_AUDIENCE}") scope: String,
        mdcValuesPropagatingClientInterceptor: MdcValuesPropagatingClientInterceptor,
        consumerIdClientInterceptor: ConsumerIdClientInterceptor,
    ): RestOperations =
        lagTokenXRestTemplate(tokenXClient, scope, mdcValuesPropagatingClientInterceptor, consumerIdClientInterceptor)
            .build()

    @Bean("pdlClientCredentialRestTemplate")
    fun pdlClientCredentialRestTemplate(
        entraIDClient: EntraIDClient,
        @Value("\${PDL_SCOPE}") scope: String,
        mdcValuesPropagatingClientInterceptor: MdcValuesPropagatingClientInterceptor,
        consumerIdClientInterceptor: ConsumerIdClientInterceptor,
    ): RestOperations =
        RestTemplateBuilder()
            .connectTimeout(Duration.of(5, ChronoUnit.SECONDS))
            .readTimeout(Duration.of(25, ChronoUnit.SECONDS))
            .messageConverters(JacksonJsonHttpMessageConverter(kontraktJsonMapper))
            .interceptors(
                MaskinTilMaskinTokenInterceptor(entraIDClient, scope),
                mdcValuesPropagatingClientInterceptor,
                consumerIdClientInterceptor,
            ).build()

    @Bean("utenAuth")
    fun restTemplateUtenAuth(
        consumerIdClientInterceptor: ConsumerIdClientInterceptor,
        mdcValuesPropagatingClientInterceptor: MdcValuesPropagatingClientInterceptor,
    ): RestOperations =
        RestTemplateBuilder()
            .connectTimeout(Duration.of(5, ChronoUnit.SECONDS))
            .readTimeout(Duration.of(25, ChronoUnit.SECONDS))
            .additionalMessageConverters(listOf(JacksonJsonHttpMessageConverter(kontraktJsonMapper)) + RestTemplate().messageConverters)
            .additionalInterceptors(
                consumerIdClientInterceptor,
                mdcValuesPropagatingClientInterceptor,
            ).build()

    private fun lagTokenXRestTemplate(
        tokenXClient: TokenXClient,
        scope: String,
        mdcValuesPropagatingClientInterceptor: MdcValuesPropagatingClientInterceptor,
        consumerIdClientInterceptor: ConsumerIdClientInterceptor,
    ): RestTemplateBuilder =
        RestTemplateBuilder()
            .connectTimeout(Duration.of(5, ChronoUnit.SECONDS))
            .readTimeout(Duration.of(25, ChronoUnit.SECONDS))
            .messageConverters(JacksonJsonHttpMessageConverter(kontraktJsonMapper))
            .interceptors(
                TokenXInterceptor(tokenXClient, scope) { EksternBrukerUtils.getBearerTokenForLoggedInUser() },
                mdcValuesPropagatingClientInterceptor,
                consumerIdClientInterceptor,
            )
}

private fun RestTemplateBuilder.withByteArrayConverterForPdf(): RestTemplateBuilder {
    val converter = ByteArrayHttpMessageConverter()
    converter.supportedMediaTypes = listOf(MediaType.APPLICATION_PDF, MediaType.APPLICATION_OCTET_STREAM)
    return this.additionalMessageConverters(converter)
}
