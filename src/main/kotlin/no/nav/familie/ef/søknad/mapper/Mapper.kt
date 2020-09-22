package no.nav.familie.ef.søknad.mapper

import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import org.slf4j.LoggerFactory

abstract class Mapper<T, R>(val rootLabel: String) {

    private val secureLogger = LoggerFactory.getLogger("secureLogger")

    fun map(data: T): Søknadsfelt<R> {
        try {
            return Søknadsfelt(rootLabel, mapDto(data))
        } catch (e: Exception) {
            secureLogger.error("Feil ved mapping av ${javaClass.simpleName}: $data")
            throw e
        }
    }

    protected abstract fun mapDto(data: T): R
}
