package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.ef.søknad.mapper.dokumentfelt
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Dokument
import no.nav.familie.kontrakter.ef.søknad.Sivilstandsdetaljer
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt

object SivilstandsdetaljerMapper {

    fun mapSivilstandsdetaljer(frontendDto: SøknadDto, dokumentMap: Map<String, Dokument>): Sivilstandsdetaljer {
        val sivilstatus = frontendDto.sivilstatus
        return Sivilstandsdetaljer(samlivsbruddsdokumentasjon = lagSamlivsbruddsdokumentasjonSøknadsfelt(dokumentMap),
                                   samlivsbruddsdato = sivilstatus.datoForSamlivsbrudd?.tilSøknadsfelt(),
                                   endringSamværsordningDato = sivilstatus.datoEndretSamvær?.tilSøknadsfelt(),
                                   fraflytningsdato = sivilstatus.datoFlyttetFraHverandre?.tilSøknadsfelt(),
                                   erUformeltGift = sivilstatus.erUformeltGift.tilSøknadsfelt(),
                                   erUformeltGiftDokumentasjon = dokumentfelt("Gift i utlandet", dokumentMap), //TODO vedlegg
                                   separasjonsbekreftelse = dokumentfelt("separasjonsbekreftelse", dokumentMap),
                                   erUformeltSeparertEllerSkilt = sivilstatus.erUformeltSeparertEllerSkilt.tilSøknadsfelt(),
                                   erUformeltSeparertEllerSkiltDokumentasjon = dokumentfelt("separertEllerSkiltIUtlandetDokumentasjon",
                                                                                           dokumentMap),//TODO vedlegg
                                   datoSøktSeparasjon = sivilstatus.datoSøktSeparasjon?.tilSøknadsfelt(),
                                   søktOmSkilsmisseSeparasjon = sivilstatus.harSøktSeparasjon.tilSøknadsfelt(),
                                   årsakEnslig = sivilstatus.årsakEnslig?.tilSøknadsfelt() // TODO stemmer dette?
        )
    }

    private fun lagSamlivsbruddsdokumentasjonSøknadsfelt(dokumenter: Map<String, Dokument>): Søknadsfelt<Dokument>? {
        return dokumentfelt("samlivsbrudd", dokumenter)
    }

}
