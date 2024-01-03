package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Bosituasjon
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.TekstFelt
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.DokumentfeltUtil.dokumentfelt
import no.nav.familie.ef.søknad.mapper.MapperMedVedlegg
import no.nav.familie.ef.søknad.mapper.Språktekster
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.BOR_PÅ_ULIKE_ADRESSER
import no.nav.familie.ef.søknad.mapper.tilNullableBooleanFelt
import no.nav.familie.ef.søknad.mapper.tilNullableDatoFelt
import no.nav.familie.ef.søknad.mapper.tilNullableTekstFelt
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
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
