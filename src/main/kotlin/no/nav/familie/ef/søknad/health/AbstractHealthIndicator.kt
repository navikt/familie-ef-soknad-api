package no.nav.familie.ef.søknad.health

import no.nav.familie.ef.søknad.config.HealthIndicatorConfig
import no.nav.familie.ef.søknad.integration.PingableRestClient
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator

internal abstract class AbstractHealthIndicator(private val pingable: PingableRestClient,
                                                private val healthIndicatorConfig: HealthIndicatorConfig) : HealthIndicator {


    override fun health(): Health {
        return try {
            pingable.ping()
            up()
        } catch (e: Exception) {
            down(e)
        }
    }

    private fun up(): Health {
        return if (healthIndicatorConfig.detailed) {
            Health.up()
                    .withDetail(pingable::class.simpleName, pingable.pingUri)
                    .build()
        } else {
            Health.up().build()
        }
    }

    private fun down(e: Exception): Health {
        return if (healthIndicatorConfig.detailed) {
            Health.down()
                    .withDetail(pingable::class.simpleName, pingable.pingUri)
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
