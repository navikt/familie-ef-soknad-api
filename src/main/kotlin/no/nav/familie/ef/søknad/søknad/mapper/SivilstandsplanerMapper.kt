package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.person.mapper.PersonMinimumMapper
import no.nav.familie.ef.søknad.søknad.domain.Bosituasjon
import no.nav.familie.ef.søknad.utils.Språktekster.Fremtidsplaner
import no.nav.familie.ef.søknad.utils.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Sivilstandsplaner

object SivilstandsplanerMapper : Mapper<Bosituasjon, Sivilstandsplaner>(Fremtidsplaner) {
    override fun mapDto(data: Bosituasjon): Sivilstandsplaner {
        return Sivilstandsplaner(
            harPlaner = data.skalGifteSegEllerBliSamboer?.tilSøknadsfelt(),
            fraDato = data.datoSkalGifteSegEllerBliSamboer?.tilSøknadsfelt(),
            vordendeSamboerEktefelle = data.vordendeSamboerEktefelle?.let(PersonMinimumMapper::map),
        )
    }
}
