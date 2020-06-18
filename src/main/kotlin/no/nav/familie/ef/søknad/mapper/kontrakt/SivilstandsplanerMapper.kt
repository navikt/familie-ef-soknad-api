package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Bosituasjon
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Sivilstandsplaner

object SivilstandsplanerMapper {

    fun mapSivilstandsplaner(bosituasjon: Bosituasjon): Sivilstandsplaner {
        return Sivilstandsplaner(harPlaner = bosituasjon.skalGifteSegEllerBliSamboer?.tilSøknadsfelt(),
                                 fraDato = bosituasjon.datoSkalGifteSegEllerBliSamboer?.tilSøknadsfelt(),
                                 vordendeSamboerEktefelle = bosituasjon.samboerDetaljer?.let(PersonMinimumMapper::map))
    }

}
