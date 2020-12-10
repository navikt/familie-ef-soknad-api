package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Sivilstatus
import no.nav.familie.ef.søknad.mapper.DokumentasjonWrapper
import no.nav.familie.ef.søknad.mapper.DokumentfeltUtil.dokumentfelt
import no.nav.familie.ef.søknad.mapper.MapperMedVedlegg
import no.nav.familie.ef.søknad.mapper.hentTekst
import no.nav.familie.ef.søknad.mapper.kontrakt.DokumentIdentifikator.*
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Sivilstandsdetaljer
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt

object SivilstandsdetaljerMapper : MapperMedVedlegg<Sivilstatus, Sivilstandsdetaljer>("Årsak til alene med barn".hentTekst()) {


    override fun mapDto(sivilstatus: Sivilstatus,
                        vedlegg: Map<String, DokumentasjonWrapper>): Sivilstandsdetaljer {
        return Sivilstandsdetaljer(samlivsbruddsdokumentasjon = dokumentfelt(SAMLIVSBRUDD, vedlegg),
                                   samlivsbruddsdato = sivilstatus.datoForSamlivsbrudd?.tilSøknadsfelt(),
                                   endringSamværsordningDato = sivilstatus.datoEndretSamvær?.tilSøknadsfelt(),
                                   fraflytningsdato = sivilstatus.datoFlyttetFraHverandre?.tilSøknadsfelt(),
                                   erUformeltGift = sivilstatus.erUformeltGift?.tilSøknadsfelt(),
                                   erUformeltGiftDokumentasjon = dokumentfelt(INNGÅTT_EKTESKAP, vedlegg),
                                   separasjonsbekreftelse = dokumentfelt(SEPARASJON_ELLER_SKILSMISSE, vedlegg),
                                   erUformeltSeparertEllerSkilt = sivilstatus.erUformeltSeparertEllerSkilt?.tilSøknadsfelt(),
                                   erUformeltSeparertEllerSkiltDokumentasjon =
                                   dokumentfelt(UFORMELL_SEPARASJON_ELLER_SKILSMISSE, vedlegg),
                                   datoSøktSeparasjon = sivilstatus.datoSøktSeparasjon?.tilSøknadsfelt(),
                                   søktOmSkilsmisseSeparasjon = sivilstatus.harSøktSeparasjon?.tilSøknadsfelt(),
                                   årsakEnslig = sivilstatus.årsakEnslig?.tilSøknadsfelt(),
                                   tidligereSamboerdetaljer = sivilstatus.tidligereSamboerDetaljer?.let {
                                       Søknadsfelt("Om den tidligere samboeren din".hentTekst(),
                                                   PersonMinimumMapper.personMinimum(it))
                                   })
    }

}
