package no.nav.familie.ef.søknad.config

import no.nav.familie.http.interceptor.BearerTokenClientCredentialsClientInterceptor
import no.nav.familie.http.interceptor.BearerTokenExchangeClientInterceptor
import no.nav.familie.http.interceptor.ConsumerIdClientInterceptor
import no.nav.familie.http.interceptor.MdcValuesPropagatingClientInterceptor
import no.nav.security.token.support.client.spring.oauth2.EnableOAuth2Client
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.web.client.RestOperations
import java.time.Duration
import java.time.temporal.ChronoUnit

@Configuration
@EnableOAuth2Client(cacheEnabled = true)
@Import(MdcValuesPropagatingClientInterceptor::class,
    ConsumerIdClientInterceptor::class,
    BearerTokenExchangeClientInterceptor::class,
    BearerTokenClientCredentialsClientInterceptor::class)
class SøknadClientConfig {

    @Bean("patientTokenExchange")
    fun restTemplate(bearerTokenExchangeClientInterceptor: BearerTokenExchangeClientInterceptor,
                     mdcValuesPropagatingClientInterceptor: MdcValuesPropagatingClientInterceptor,
                     consumerIdClientInterceptor: ConsumerIdClientInterceptor
    ): RestOperations {
        return RestTemplateBuilder()
            .setConnectTimeout(Duration.of(10, ChronoUnit.SECONDS))
            .setReadTimeout(Duration.of(25, ChronoUnit.SECONDS))
            .interceptors(bearerTokenExchangeClientInterceptor,
                mdcValuesPropagatingClientInterceptor,
                consumerIdClientInterceptor)
            .build()
    }
}