package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Bosituasjon
import no.nav.familie.kontrakter.ef.søknad.PersonMinimum
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import java.time.LocalDate
import no.nav.familie.kontrakter.ef.søknad.Bosituasjon as KontraktBosituasjon

object BosituasjonMapper {
    fun mapBosituasjon(bosituasjon: Bosituasjon): KontraktBosituasjon {
        return KontraktBosituasjon(mapSøkerDelerBoligMedAndre(bosituasjon),
                                   mapSamboer(bosituasjon),
                                   mapDatoFlyttetSammenMedSamboer(bosituasjon))
    }

    private fun mapSøkerDelerBoligMedAndre(bosituasjon: Bosituasjon) =
            Søknadsfelt(bosituasjon.søkerDelerBoligMedAndreVoksne.label,
                        bosituasjon.søkerDelerBoligMedAndreVoksne.verdi)

    private fun mapDatoFlyttetSammenMedSamboer(bosituasjon: Bosituasjon): Søknadsfelt<LocalDate>? {
        return bosituasjon.datoFlyttetSammenMedSamboer?.let {
            Søknadsfelt(it.label, it.verdi)
        }
    }

    private fun mapSamboer(bosituasjon: Bosituasjon): Søknadsfelt<PersonMinimum>? {
        return bosituasjon.samboerDetaljer?.let {
            PersonMinimumMapper.map(it)
        }
    }


}
