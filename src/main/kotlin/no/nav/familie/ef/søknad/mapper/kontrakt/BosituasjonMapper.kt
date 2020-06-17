package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Bosituasjon
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.DatoFelt
import no.nav.familie.ef.søknad.mapper.dokumentfelt
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.PersonMinimum
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Vedlegg
import java.time.LocalDate
import no.nav.familie.kontrakter.ef.søknad.Bosituasjon as KontraktBosituasjon

object BosituasjonMapper {
    fun mapBosituasjon(bosituasjon: Bosituasjon, vedlegg: Map<String, List<Vedlegg>>): KontraktBosituasjon {
        return KontraktBosituasjon(delerDuBolig = mapSøkerDelerBoligMedAndre(bosituasjon),
                                   samboerdetaljer = mapSamboer(bosituasjon),
                                   sammenflyttingsdato = mapDatoFlyttetSammenMedSamboer(bosituasjon),
                                   tidligereSamboerFortsattRegistrertPåAdresse = dokumentfelt(BOR_PÅ_ULIKE_ADRESSER, vedlegg))
    }

    private fun mapSøkerDelerBoligMedAndre(bosituasjon: Bosituasjon) = bosituasjon.delerBoligMedAndreVoksne.tilSøknadsfelt()

    private fun mapDatoFlyttetSammenMedSamboer(bosituasjon: Bosituasjon): Søknadsfelt<LocalDate>? {
        return bosituasjon.datoFlyttetSammenMedSamboer?.let(DatoFelt::tilSøknadsfelt)
    }

    private fun mapSamboer(bosituasjon: Bosituasjon): Søknadsfelt<PersonMinimum>? {
        return bosituasjon.samboerDetaljer?.let {
            PersonMinimumMapper.map(it)
        }
    }


}
