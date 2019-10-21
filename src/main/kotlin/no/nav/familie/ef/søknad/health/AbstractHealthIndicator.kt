package no.nav.familie.ef.søknad.health

import io.micrometer.core.instrument.Counter
import no.nav.familie.ef.søknad.config.HealthIndicatorConfig
import no.nav.familie.ef.søknad.integration.PingableRestClient
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator

internal abstract class AbstractHealthIndicator(private val pingable: PingableRestClient,
                                                private val healthIndicatorConfig: HealthIndicatorConfig) : HealthIndicator {

    protected abstract val successCounter: Counter
    protected abstract val failureCounter: Counter


    override fun health(): Health {
        return try {
            pingable.ping()
            successCounter.increment()
            up()
        } catch (e: Exception) {
            failureCounter.increment()
            down(e)
        }
    }

    private fun up(): Health {
        return if (healthIndicatorConfig.detailed) {
            Health.up()
                    .withDetail(pingable::class.simpleName, pingable.pingURI())
                    .build()
        } else {
            Health.up().build()
        }
    }

    private fun down(e: Exception): Health {
        return if (healthIndicatorConfig.detailed) {
            Health.down()
                    .withDetail(pingable::class.simpleName, pingable.pingURI())
                    .withException(e)
                    .build()
        } else {
            Health.down().build()
        }
    }

    override fun toString(): String {
        return "AbstractHealthIndicator(pingable=$pingable, healthIndicatorConfig=$healthIndicatorConfig)"
    }

}
