package no.nav.familie.ef.søknad.config

import no.finn.unleash.DefaultUnleash
import no.finn.unleash.UnleashContext
import no.finn.unleash.UnleashContextProvider
import no.finn.unleash.util.UnleashConfig
import no.nav.familie.ef.søknad.featuretoggle.ByEnvironmentStrategy
import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Bean

@ConfigurationProperties("funksjonsbrytere")
@ConstructorBinding
class FeatureToggleConfig(val enabled: Boolean,
                          val unleash: Unleash) {

    @ConstructorBinding
    data class Unleash(val apiUrl: String,
                       val env: String,
                       val appName: String)

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun featureToggle(): FeatureToggleService =
            if (enabled)
                lagUnleashFeatureToggleService()
            else {
                log.warn("Funksjonsbryter-funksjonalitet er skrudd AV. " +
                         "Gir standardoppførsel for alle funksjonsbrytere, dvs 'false'")
                lagDummyFeatureToggleService()
            }

    private fun lagUnleashFeatureToggleService(): FeatureToggleService {
        val unleash = DefaultUnleash(UnleashConfig.builder()
                                             .appName(unleash.appName)
                                             .unleashAPI(unleash.apiUrl)
                                             .unleashContextProvider(lagUnleashContextProvider())
                                             .build(), ByEnvironmentStrategy())

        return object : FeatureToggleService {
            override fun isEnabled(toggleId: String, defaultValue: Boolean): Boolean {
                return unleash.isEnabled(toggleId, defaultValue)
            }
        }
    }

    private fun lagUnleashContextProvider(): UnleashContextProvider {
        return UnleashContextProvider {
            UnleashContext.builder()
                    //.userId("a user") // Må legges til en gang i fremtiden
                    .environment(unleash.env)
                    .appName(unleash.appName)
                    .build()
        }
    }

    private fun lagDummyFeatureToggleService(): FeatureToggleService {
        return object : FeatureToggleService {
            override fun isEnabled(toggleId: String, defaultValue: Boolean): Boolean {
                return defaultValue
            }
        }
    }

}

