package no.nav.familie.ef.soknad.featuretoggle

import no.finn.unleash.*
import no.finn.unleash.strategy.Strategy
import no.finn.unleash.util.UnleashConfig
import org.slf4j.LoggerFactory
import java.net.URI

class UnleashServiceImpl private constructor(val unleash: Unleash) : UnleashService {

    companion object {
        protected val LOG = LoggerFactory.getLogger(UnleashServiceImpl::class.java)

        fun nyFraApiUrl(unleashApiUrl: String): UnleashService {
            var uri : URI
            try {
                uri = URI.create(unleashApiUrl);
            } catch (e: Throwable) {
                LOG.warn("Ugyldig url til unleash-api'et: '" + unleashApiUrl + "'. Bruker FakeUnleash", e)
                return UnleashServiceImpl(FakeUnleash())
            }
                return nyFraUnleash(DefaultUnleash(UnleashConfig.builder()
                    .appName("familie.ensligforsoerger.soeknadsdialog")
                    .unleashAPI(uri)
                    .build(),ByEnvironmentStrategy()))
        }

        fun nyFraUnleash(unleash: Unleash): UnleashService {
            return UnleashServiceImpl(unleash);
        }

    }

    override fun isEnabled(toggleId: String): Boolean {
        return isEnabled(toggleId, false)
    }

    override fun isEnabled(toggleId: String, defaultValue: Boolean): Boolean {
        val unleashContext = UnleashContext.builder()
                .userId("a user")
                .environment("prod")
                .build();


        return unleash.isEnabled(toggleId, unleashContext, defaultValue);
    }

}