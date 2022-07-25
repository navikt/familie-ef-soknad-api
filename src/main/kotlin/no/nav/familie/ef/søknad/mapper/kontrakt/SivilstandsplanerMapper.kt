package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Bosituasjon
import no.nav.familie.ef.søknad.mapper.Mapper
import no.nav.familie.ef.søknad.mapper.Språktekster.Fremtidsplaner
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Sivilstandsplaner

object SivilstandsplanerMapper : Mapper<Bosituasjon, Sivilstandsplaner>(Fremtidsplaner) {

    override fun mapDto(data: Bosituasjon): Sivilstandsplaner {
        return Sivilstandsplaner(
            harPlaner = data.skalGifteSegEllerBliSamboer?.tilSøknadsfelt(),
            fraDato = data.datoSkalGifteSegEllerBliSamboer?.tilSøknadsfelt(),
            vordendeSamboerEktefelle = data.vordendeSamboerEktefelle?.let(PersonMinimumMapper::map)
        )
    }
}
