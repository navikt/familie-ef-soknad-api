package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Bosituasjon
import no.nav.familie.ef.søknad.mapper.Mapper
import no.nav.familie.ef.søknad.mapper.Språktekster.Fremtidsplaner
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Sivilstandsplaner

object SivilstandsplanerMapper : Mapper<Bosituasjon, Sivilstandsplaner>(Fremtidsplaner) {

    override fun mapDto(bosituasjon: Bosituasjon): Sivilstandsplaner {
        return Sivilstandsplaner(harPlaner = bosituasjon.skalGifteSegEllerBliSamboer?.tilSøknadsfelt(),
                                 fraDato = bosituasjon.datoSkalGifteSegEllerBliSamboer?.tilSøknadsfelt(),
                                 vordendeSamboerEktefelle = bosituasjon.skalGifteSegEllerBliSamboer?.let {
                                     when (it.verdi) {
                                         true -> bosituasjon.samboerDetaljer?.let(PersonMinimumMapper::map)
                                         false -> null
                                     }
                                 })
    }

}
