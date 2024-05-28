package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.utils.Språktekster
import no.nav.familie.ef.søknad.utils.hentTekst
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import org.slf4j.LoggerFactory

abstract class Mapper<T, R>(private val rootLabel: Språktekster) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun map(data: T): Søknadsfelt<R> {
        return Søknadsfelt(rootLabel.hentTekst(), mapDto(data))
    }

    protected abstract fun mapDto(data: T): R
}
