package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.BooleanFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Bosituasjon
import no.nav.familie.kontrakter.ef.søknad.PersonMinimum
import no.nav.familie.kontrakter.ef.søknad.Sivilstandsplaner
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import java.time.LocalDate

object SivilstandsplanerMapper {

    fun mapSivilstandsplaner(bosituasjon: Bosituasjon): Sivilstandsplaner {
        return Sivilstandsplaner(harPlaner = mapSøkerSkalGifteSeg(bosituasjon.skalGifteSegEllerBliSamboer),
                                 fraDato = mapSivistandsplanerDato(bosituasjon),
                                 vordendeSamboerEktefelle = mapPersonMinimum(bosituasjon))
    }

    private fun mapPersonMinimum(bosituasjon: Bosituasjon): Søknadsfelt<PersonMinimum>? {
        return bosituasjon.samboerDetaljer?.let { PersonMinimumMapper.map(it) }
    }

    private fun mapSivistandsplanerDato(bosituasjon: Bosituasjon): Søknadsfelt<LocalDate>? {
        return bosituasjon.datoSkalGifteSegEllerBliSamboer?.let {
            Søknadsfelt(it.label, it.verdi)
        }
    }

    private fun mapSøkerSkalGifteSeg(skalGifteSegEllerBliSamboer: BooleanFelt) =
                Søknadsfelt(skalGifteSegEllerBliSamboer.label, skalGifteSegEllerBliSamboer.verdi)

}
