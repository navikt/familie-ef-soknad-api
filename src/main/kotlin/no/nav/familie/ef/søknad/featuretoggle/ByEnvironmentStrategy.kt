package no.nav.familie.ef.søknad.featuretoggle


import no.finn.unleash.UnleashContext
import no.finn.unleash.strategy.Strategy

class ByEnvironmentStrategy : Strategy {

    companion object {
        private val miljøKey = "miljø";

        fun miljø(vararg strings : String) : Map<String, String> {
            return mapOf(miljøKey to strings.joinToString(","));
        }
    }

    override fun getName(): String {
        return "byEnvironmentStrategy"
    }

    override fun isEnabled(map: Map<String, String>?): Boolean {
        return isEnabled(map,UnleashContext.builder().build());
    }

    override fun isEnabled(map: Map<String, String>?, unleashContext : UnleashContext): Boolean {

        return unleashContext.environment
                .map { env->map?.get(miljøKey)?.split(',')?.contains(env) ?: false  }
                .orElse(false);
    }

}