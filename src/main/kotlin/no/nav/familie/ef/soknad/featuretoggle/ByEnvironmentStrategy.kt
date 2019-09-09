package no.nav.familie.ef.soknad.featuretoggle


import no.finn.unleash.UnleashContext
import no.finn.unleash.strategy.Strategy
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import javax.inject.Inject
import java.util.Arrays

import java.util.Optional.ofNullable

class ByEnvironmentStrategy : Strategy {

    override fun getName(): String {
        return "byEnvironment"
    }

    override fun isEnabled(map: Map<String, String>): Boolean {
        return false;
    }

    override fun isEnabled(map: Map<String, String>,unleashContext : UnleashContext): Boolean {
        return ofNullable(map)
                .map { m -> m["miljø"] }
                .map { s -> s?.split(',') }
                .map { l->l?.contains(unleashContext.environment ?: "")}
                .map { b-> b ?: false }
                .orElse(false)
    }

}