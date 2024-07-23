package no.nav.familie.ef.søknad.infrastruktur.config

import io.getunleash.strategy.Strategy
import no.nav.familie.ef.søknad.infrastruktur.featuretoggle.ByEnvironmentStrategy
import no.nav.familie.unleash.DefaultUnleashService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeatureToggleConfig(
    @Value("\${UNLEASH_SERVER_API_URL}") private val apiUrl: String,
    @Value("\${UNLEASH_SERVER_API_TOKEN}") private val apiToken: String,
    @Value("\${NAIS_APP_NAME}") private val appName: String,
) {
    @Bean
    fun strategies(): List<Strategy> = listOf(ByEnvironmentStrategy())

    @Bean
    fun defaultUnleashService(strategies: List<Strategy>): DefaultUnleashService = DefaultUnleashService(apiUrl, apiToken, appName, strategies)
}
