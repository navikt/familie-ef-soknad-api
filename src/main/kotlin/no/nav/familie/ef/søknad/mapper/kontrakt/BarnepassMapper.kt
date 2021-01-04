package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.mapper.*
import no.nav.familie.ef.søknad.mapper.Språktekster.Barnepassordninger
import no.nav.familie.ef.søknad.mapper.Språktekster.OmBarnepassordning
import no.nav.familie.kontrakter.ef.søknad.Barnepass
import no.nav.familie.kontrakter.ef.søknad.BarnepassOrdning
import no.nav.familie.kontrakter.ef.søknad.Datoperiode
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Barnepass as BarnepassDto

object BarnepassMapper : Mapper<BarnepassDto, Barnepass>(OmBarnepassordning) {

    override fun mapDto(barnepass: BarnepassDto): Barnepass {
        return Barnepass(årsakBarnepass = barnepass.årsakBarnepass?.tilSøknadsfelt(),
                         barnepassordninger = Søknadsfelt(Barnepassordninger.hentTekst(),
                                                          barnepass.barnepassordninger.map {
                                                              BarnepassOrdning(hvaSlagsBarnepassOrdning = it.hvaSlagsBarnepassOrdning.tilSøknadsfelt(),
                                                                               navn = it.navn.tilSøknadsfelt(),
                                                                               datoperiode = Søknadsfelt(it.periode.label
                                                                                                         ?: error("Savner label"),
                                                                                                         Datoperiode(it.periode.fra.tilLocalDate(),
                                                                                                                     it.periode.til.tilLocalDate())),
                                                                               belop = it.belop.tilSøknadsfelt(String::tilDesimaltall))
                                                          }))
    }

}
