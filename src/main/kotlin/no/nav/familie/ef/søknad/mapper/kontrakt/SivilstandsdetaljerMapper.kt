package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.ef.søknad.mapper.dokumentfelt
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Dokument
import no.nav.familie.kontrakter.ef.søknad.Sivilstandsdetaljer

object SivilstandsdetaljerMapper {

    fun mapSivilstandsdetaljer(frontendDto: SøknadDto, dokumentMap: Map<String, List<Dokument>>): Sivilstandsdetaljer {
        val sivilstatus = frontendDto.sivilstatus
        return Sivilstandsdetaljer(samlivsbruddsdokumentasjon = dokumentfelt(SAMLIVSBRUDD, dokumentMap),
                                   samlivsbruddsdato = sivilstatus.datoForSamlivsbrudd?.tilSøknadsfelt(),
                                   endringSamværsordningDato = sivilstatus.datoEndretSamvær?.tilSøknadsfelt(),
                                   fraflytningsdato = sivilstatus.datoFlyttetFraHverandre?.tilSøknadsfelt(),
                                   erUformeltGift = sivilstatus.erUformeltGift?.tilSøknadsfelt(),
                                   erUformeltGiftDokumentasjon = dokumentfelt(INNGÅTT_EKTESKAP, dokumentMap),
                                   separasjonsbekreftelse = dokumentfelt(SEPARASJON_ELLER_SKILSMISSE, dokumentMap),
                                   erUformeltSeparertEllerSkilt = sivilstatus.erUformeltSeparertEllerSkilt?.tilSøknadsfelt(),
                                   erUformeltSeparertEllerSkiltDokumentasjon = dokumentfelt(UFORMELL_SEPARASJON_ELLER_SKILSMISSE,
                                                                                           dokumentMap),
                                   datoSøktSeparasjon = sivilstatus.datoSøktSeparasjon?.tilSøknadsfelt(),
                                   søktOmSkilsmisseSeparasjon = sivilstatus.harSøktSeparasjon?.tilSøknadsfelt(),
                                   årsakEnslig = sivilstatus.årsakEnslig?.tilSøknadsfelt()
        )
    }

}
