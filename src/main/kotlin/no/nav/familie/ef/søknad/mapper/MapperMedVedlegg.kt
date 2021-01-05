package no.nav.familie.ef.søknad.mapper

import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import org.slf4j.LoggerFactory

abstract class MapperMedVedlegg<T, R>(private val rootLabel: Språktekster) {

    private val secureLogger = LoggerFactory.getLogger("secureLogger")

    fun map(data: T, vedlegg: Map<String, DokumentasjonWrapper>): Søknadsfelt<R> {
        try {
            return Søknadsfelt(rootLabel.hentTekst(), mapDto(data, vedlegg))
        } catch (e: Exception) {
            secureLogger.error("Feil ved mapping av ${javaClass.simpleName}: $data")
            throw e
        }
    }

    protected abstract fun mapDto(data: T, vedlegg: Map<String, DokumentasjonWrapper>): R

}
