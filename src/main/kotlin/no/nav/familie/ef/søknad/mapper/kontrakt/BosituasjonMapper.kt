package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Bosituasjon
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.MapperMedVedlegg
import no.nav.familie.ef.søknad.mapper.dokumentfelt
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.BOR_PÅ_ULIKE_ADRESSER
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.PersonMinimum
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Bosituasjon as KontraktBosituasjon

object BosituasjonMapper : MapperMedVedlegg<Bosituasjon, KontraktBosituasjon>("Bosituasjonen din") {

    override fun mapDto(bosituasjon: Bosituasjon, vedlegg: Map<String, DokumentasjonWrapper>): KontraktBosituasjon {
        return KontraktBosituasjon(delerDuBolig = mapSøkerDelerBoligMedAndre(bosituasjon),
                                   samboerdetaljer = mapSamboer(bosituasjon),
                                   sammenflyttingsdato = bosituasjon.datoFlyttetSammenMedSamboer?.tilSøknadsfelt(),
                                   tidligereSamboerFortsattRegistrertPåAdresse = dokumentfelt(BOR_PÅ_ULIKE_ADRESSER, vedlegg),
                                   datoFlyttetFraHverandre = bosituasjon.datoFlyttetFraHverandre?.tilSøknadsfelt())
    }

    private fun mapSøkerDelerBoligMedAndre(bosituasjon: Bosituasjon) = bosituasjon.delerBoligMedAndreVoksne.tilSøknadsfelt()

    private fun mapSamboer(bosituasjon: Bosituasjon): Søknadsfelt<PersonMinimum>? {
        return bosituasjon.samboerDetaljer?.let {
            PersonMinimumMapper.map(it)
        }
    }
}
