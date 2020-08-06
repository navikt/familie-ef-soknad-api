package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.mapper.tilDesimaltall
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Barnepass
import no.nav.familie.kontrakter.ef.søknad.BarnepassOrdning
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Barnepass as BarnepassDto

object BarnepassMapper {

    fun tilBarnepass(barnepass: BarnepassDto): Barnepass {
        return Barnepass(årsakBarnepass = barnepass.årsakBarnepass?.tilSøknadsfelt(),
                         barnepassordninger = Søknadsfelt("Barnepassordninger", barnepass.barnepassordninger.map { // TODO label?
                             BarnepassOrdning(hvaSlagsBarnepassOrdning = it.hvaSlagsBarnepassOrdning.tilSøknadsfelt(),
                                              navn = it.navn.tilSøknadsfelt(),
                                              periode = it.periode.tilSøknadsfelt(),
                                              belop = it.belop.tilSøknadsfelt(String::tilDesimaltall))
                         }))
    }
}