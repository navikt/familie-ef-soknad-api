package no.nav.familie.ef.søknad.config

import no.finn.unleash.DefaultUnleash
import no.finn.unleash.UnleashContext
import no.finn.unleash.UnleashContextProvider
import no.finn.unleash.util.UnleashConfig
import no.nav.familie.ef.søknad.featuretoggle.ByEnvironmentStrategy
import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeatureToggleConfig(@Value("\${familie.ef.funksjonsbrytere.enabled}") val enabled: Boolean,
                          @Value("\${familie.ef.funksjonsbrytere.unleash.apiUrl:}") val unleashApiUrl: String,
                          @Value("\${familie.ef.funksjonsbrytere.unleash.environment:}") val unleashEnv: String,
                          @Value("\${familie.ef.funksjonsbrytere.unleash.applicationName:}") val unleashAppName: String) {

    protected val log: Logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun featureToggle(): FeatureToggleService =
            if (enabled)
                lagUnleashFeatureToggleService()
            else {
                log.warn("Funksjonsbryter-funksjonalitet er skrudd AV. Gir standardoppførsel for alle funksjonsbrytere, dvs 'false'")
                lagDummyFeatureToggleService()
            }

    private fun lagUnleashFeatureToggleService(): FeatureToggleService {
        val unleash = DefaultUnleash(UnleashConfig.builder()
                .appName(unleashAppName)
                .unleashAPI(unleashApiUrl)
                .unleashContextProvider(lagUnleashContextProvider())
                .build(), ByEnvironmentStrategy())

        return object : FeatureToggleService {
            override fun isEnabled(toggleId: String, defaultValue: Boolean): Boolean {
                return unleash.isEnabled(toggleId, defaultValue)
            }
        }
    }

    private fun lagUnleashContextProvider(): UnleashContextProvider {
        return object : UnleashContextProvider {
            override fun getContext(): UnleashContext {
                return UnleashContext.builder()
                        //.userId("a user") // Må legges til en gang i fremtiden
                        .environment(unleashEnv)
                        .appName(unleashAppName)
                        .build();
            }
        }
    }

    private fun lagDummyFeatureToggleService(): FeatureToggleService {
        return object : FeatureToggleService {
            override fun isEnabled(toggleId: String, defaultValue: Boolean): Boolean {
                if(unleashEnv == "local"){
                    return true;
                }
                return defaultValue;
            }
        }
    }

}

