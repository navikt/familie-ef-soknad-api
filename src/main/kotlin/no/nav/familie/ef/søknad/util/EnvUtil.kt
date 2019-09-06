package no.nav.familie.ef.søknad.util

import org.slf4j.Marker
import org.slf4j.MarkerFactory
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles

object EnvUtil {

    private const val NOT = "!"
    const val TEST = "test"
    private const val DEV = "dev"
    private const val DEV_GCP = "dev-gcp"

    private const val LOCAL = "local"
    private const val LOCALSTACK = "localstack"
    const val NOTLOCALSTACK = NOT + LOCALSTACK
    const val NOTLOCAL = NOT + LOCAL
    const val DEFAULT = "default"

    val CONFIDENTIAL: Marker = MarkerFactory.getMarker("CONFIDENTIAL")

    fun isDevOrLocal(env: Environment): Boolean {
        return isLocal(env) || isDev(env)
    }

    private fun isDev(env: Environment): Boolean {
        return env.acceptsProfiles(Profiles.of(DEV, DEV_GCP))
    }

    private fun isLocal(env: Environment?): Boolean {
        return env == null || env.acceptsProfiles(Profiles.of(LOCAL))
    }
}
