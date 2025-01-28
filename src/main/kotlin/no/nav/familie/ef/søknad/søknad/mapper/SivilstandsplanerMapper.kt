package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.person.mapper.PersonMinimumMapper
import no.nav.familie.ef.søknad.søknad.domain.Bosituasjon
import no.nav.familie.ef.søknad.utils.Språktekster.Fremtidsplaner
import no.nav.familie.ef.søknad.utils.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Sivilstandsplaner

object SivilstandsplanerMapper : Mapper<Bosituasjon, Sivilstandsplaner?>(Fremtidsplaner) {
    override fun mapDto(data: Bosituasjon): Sivilstandsplaner? {
        val harPlaner = data.skalGifteSegEllerBliSamboer?.tilSøknadsfelt()
        val fraDato = data.datoSkalGifteSegEllerBliSamboer?.tilSøknadsfelt()
        val vordendeSamboerEktefelle = data.vordendeSamboerEktefelle?.let(PersonMinimumMapper::map)

        return if (harPlaner == null && fraDato == null && vordendeSamboerEktefelle == null) {
            null
        } else {
            Sivilstandsplaner(
                harPlaner = harPlaner,
                fraDato = fraDato,
                vordendeSamboerEktefelle = vordendeSamboerEktefelle
            )
        }
    }
}
