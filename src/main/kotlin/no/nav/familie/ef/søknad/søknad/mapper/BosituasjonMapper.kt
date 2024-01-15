package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.person.mapper.PersonMinimumMapper
import no.nav.familie.ef.søknad.søknad.domain.Bosituasjon
import no.nav.familie.ef.søknad.søknad.domain.DokumentIdentifikator.BOR_PÅ_ULIKE_ADRESSER
import no.nav.familie.ef.søknad.søknad.domain.TekstFelt
import no.nav.familie.ef.søknad.utils.DokumentasjonWrapper
import no.nav.familie.ef.søknad.utils.DokumentfeltUtil.dokumentfelt
import no.nav.familie.ef.søknad.utils.Språktekster
import no.nav.familie.ef.søknad.utils.tilNullableBooleanFelt
import no.nav.familie.ef.søknad.utils.tilNullableDatoFelt
import no.nav.familie.ef.søknad.utils.tilNullableTekstFelt
import no.nav.familie.ef.søknad.utils.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.PersonMinimum
import no.nav.familie.kontrakter.ef.søknad.Sivilstandsplaner
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Bosituasjon as KontraktBosituasjon

object BosituasjonMapper : MapperMedVedlegg<Bosituasjon, KontraktBosituasjon>(Språktekster.Bosituasjon) {

    override fun mapDto(data: Bosituasjon, vedlegg: Map<String, DokumentasjonWrapper>): KontraktBosituasjon {
        return KontraktBosituasjon(
            delerDuBolig = mapSøkerDelerBoligMedAndre(data),
            samboerdetaljer = mapSamboer(data),
            sammenflyttingsdato = data.datoFlyttetSammenMedSamboer?.tilSøknadsfelt(),
            tidligereSamboerFortsattRegistrertPåAdresse = dokumentfelt(BOR_PÅ_ULIKE_ADRESSER, vedlegg),
            datoFlyttetFraHverandre = data.datoFlyttetFraHverandre?.tilSøknadsfelt(),
        )
    }

    private fun mapSøkerDelerBoligMedAndre(bosituasjon: Bosituasjon) = bosituasjon.delerBoligMedAndreVoksne.tilSøknadsfelt()

    private fun mapSamboer(bosituasjon: Bosituasjon): Søknadsfelt<PersonMinimum>? {
        return bosituasjon.samboerDetaljer?.let {
            PersonMinimumMapper.map(it)
        }
    }

    fun mapTilDto(bosituasjon: KontraktBosituasjon, sivilstandsplaner: Sivilstandsplaner?): Bosituasjon {
        return Bosituasjon(
            delerBoligMedAndreVoksne = bosituasjon.delerDuBolig.tilNullableTekstFelt() ?: TekstFelt("", ""),
            datoFlyttetSammenMedSamboer = bosituasjon.sammenflyttingsdato.tilNullableDatoFelt(),
            samboerDetaljer = PersonMinimumMapper.mapTilDto(bosituasjon.samboerdetaljer?.verdi),
            datoSkalGifteSegEllerBliSamboer = sivilstandsplaner?.fraDato.tilNullableDatoFelt(),
            skalGifteSegEllerBliSamboer = sivilstandsplaner?.harPlaner.tilNullableBooleanFelt(),
            datoFlyttetFraHverandre = bosituasjon.datoFlyttetFraHverandre.tilNullableDatoFelt(),
            vordendeSamboerEktefelle = PersonMinimumMapper.mapTilDto(sivilstandsplaner?.vordendeSamboerEktefelle?.verdi),
        )
    }
}
