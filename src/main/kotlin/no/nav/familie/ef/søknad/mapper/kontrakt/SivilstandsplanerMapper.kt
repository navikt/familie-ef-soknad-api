package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Bosituasjon
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Sivilstandsplaner
import org.slf4j.LoggerFactory

object SivilstandsplanerMapper {

    private val secureLogger = LoggerFactory.getLogger("secureLogger")

    fun mapSivilstandsplaner(bosituasjon: Bosituasjon): Sivilstandsplaner {
        try {
            return Sivilstandsplaner(harPlaner = bosituasjon.skalGifteSegEllerBliSamboer?.tilSøknadsfelt(),
                                     fraDato = bosituasjon.datoSkalGifteSegEllerBliSamboer?.tilSøknadsfelt(),
                                     vordendeSamboerEktefelle = bosituasjon.samboerDetaljer?.let(PersonMinimumMapper::map))
        } catch (e: Exception) {
            secureLogger.error("Feil ved mapping av bosituasjon", bosituasjon, e)
            throw e
        }
    }
}
