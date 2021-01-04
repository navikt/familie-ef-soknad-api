package no.nav.familie.ef.søknad.mapper

import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import org.slf4j.LoggerFactory

abstract class Mapper<T, R>(private val rootLabel: Språktekster) {

    private val secureLogger = LoggerFactory.getLogger("secureLogger")

    fun map(data: T): Søknadsfelt<R> {
        try {
            return Søknadsfelt(rootLabel.hentTekst(), mapDto(data))
        } catch (e: Exception) {
            secureLogger.error("Feil ved mapping av ${javaClass.simpleName}: $data")
            throw e
        }
    }

    protected abstract fun mapDto(data: T): R
}
