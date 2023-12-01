package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.BooleanFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.DatoFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Sivilstatus
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SivilstatusTilGjenbruk
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.TekstFelt
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.DokumentfeltUtil.dokumentfelt
import no.nav.familie.ef.søknad.mapper.MapperMedVedlegg
import no.nav.familie.ef.søknad.mapper.Språktekster.OmDenTidligereSamboeren
import no.nav.familie.ef.søknad.mapper.Språktekster.ÅrsakTilAleneMedBarn
import no.nav.familie.ef.søknad.mapper.hentTekst
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.INNGÅTT_EKTESKAP
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.SAMLIVSBRUDD
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.SEPARASJON_ELLER_SKILSMISSE
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.UFORMELL_SEPARASJON_ELLER_SKILSMISSE
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Sivilstandsdetaljer
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import java.time.LocalDate

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
            tidligereSamboerdetaljer = data.tidligereSamboerDetaljer?.let {
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
            datoForSamlivsbrudd = sivilstandsdetaljer.samlivsbruddsdato.tilDatoFelt(),
            datoFlyttetFraHverandre = sivilstandsdetaljer.fraflytningsdato.tilDatoFelt(),
            datoEndretSamvær = sivilstandsdetaljer.endringSamværsordningDato.tilDatoFelt(),
            tidligereSamboerDetaljer = sivilstandsdetaljer.tidligereSamboerdetaljer?.let { PersonMinimumMapper.mapTilDto(it.verdi) },
        )
    }
}

fun Søknadsfelt<LocalDate>?.tilDatoFelt() = this?.let { DatoFelt(it.label, it.verdi.toString()) }
fun Søknadsfelt<Boolean>?.tilBooleanFelt() = this?.let { BooleanFelt(it.label, it.verdi, it.svarId) }
fun Søknadsfelt<String>?.tilTekstFelt() = this?.let { TekstFelt(it.label, it.verdi, it.svarId) }
