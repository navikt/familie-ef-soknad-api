package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.person.mapper.PersonMinimumMapper
import no.nav.familie.ef.søknad.søknad.domain.DokumentIdentifikator.INNGÅTT_EKTESKAP
import no.nav.familie.ef.søknad.søknad.domain.DokumentIdentifikator.SAMLIVSBRUDD
import no.nav.familie.ef.søknad.søknad.domain.DokumentIdentifikator.SEPARASJON_ELLER_SKILSMISSE
import no.nav.familie.ef.søknad.søknad.domain.DokumentIdentifikator.UFORMELL_SEPARASJON_ELLER_SKILSMISSE
import no.nav.familie.ef.søknad.søknad.domain.Sivilstatus
import no.nav.familie.ef.søknad.søknad.domain.SivilstatusTilGjenbruk
import no.nav.familie.ef.søknad.søknad.domain.TekstFelt
import no.nav.familie.ef.søknad.utils.DokumentasjonWrapper
import no.nav.familie.ef.søknad.utils.DokumentfeltUtil.dokumentfelt
import no.nav.familie.ef.søknad.utils.Språktekster.OmDenTidligereSamboeren
import no.nav.familie.ef.søknad.utils.Språktekster.ÅrsakTilAleneMedBarn
import no.nav.familie.ef.søknad.utils.hentTekst
import no.nav.familie.ef.søknad.utils.tilNullableDatoFelt
import no.nav.familie.ef.søknad.utils.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Sivilstandsdetaljer
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt

object SivilstandsdetaljerMapper : MapperMedVedlegg<Sivilstatus, Sivilstandsdetaljer>(ÅrsakTilAleneMedBarn) {
    override fun mapDto(
        data: Sivilstatus,
        vedlegg: Map<String, DokumentasjonWrapper>,
    ): Sivilstandsdetaljer {
        return Sivilstandsdetaljer(
            samlivsbruddsdokumentasjon = dokumentfelt(SAMLIVSBRUDD, vedlegg),
            samlivsbruddsdato = data.datoForSamlivsbrudd?.tilSøknadsfelt(),
            endringSamværsordningDato = data.datoEndretSamvær?.tilSøknadsfelt(),
            fraflytningsdato = data.datoFlyttetFraHverandre?.tilSøknadsfelt(),
            erUformeltGift = data.erUformeltGift?.tilSøknadsfelt(),
            erUformeltGiftDokumentasjon = dokumentfelt(INNGÅTT_EKTESKAP, vedlegg),
            separasjonsbekreftelse = dokumentfelt(SEPARASJON_ELLER_SKILSMISSE, vedlegg),
            erUformeltSeparertEllerSkilt = data.erUformeltSeparertEllerSkilt?.tilSøknadsfelt(),
            erUformeltSeparertEllerSkiltDokumentasjon =
                dokumentfelt(UFORMELL_SEPARASJON_ELLER_SKILSMISSE, vedlegg),
            datoSøktSeparasjon = data.datoSøktSeparasjon?.tilSøknadsfelt(),
            søktOmSkilsmisseSeparasjon = data.harSøktSeparasjon?.tilSøknadsfelt(),
            årsakEnslig = data.årsakEnslig?.tilSøknadsfelt(),
            tidligereSamboerdetaljer =
                data.tidligereSamboerDetaljer?.let {
                    Søknadsfelt(
                        OmDenTidligereSamboeren.hentTekst(),
                        PersonMinimumMapper.personMinimum(it),
                    )
                },
        )
    }

    fun mapTilDto(sivilstandsdetaljer: Sivilstandsdetaljer): SivilstatusTilGjenbruk {
        return SivilstatusTilGjenbruk(
            årsakEnslig = sivilstandsdetaljer.årsakEnslig?.let { TekstFelt(it.label, it.verdi, it.svarId) },
            datoForSamlivsbrudd = sivilstandsdetaljer.samlivsbruddsdato.tilNullableDatoFelt(),
            datoFlyttetFraHverandre = sivilstandsdetaljer.fraflytningsdato.tilNullableDatoFelt(),
            datoEndretSamvær = sivilstandsdetaljer.endringSamværsordningDato.tilNullableDatoFelt(),
            tidligereSamboerDetaljer =
                sivilstandsdetaljer.tidligereSamboerdetaljer?.let {
                    PersonMinimumMapper.mapTilDto(
                        it.verdi,
                    )
                },
        )
    }
}
