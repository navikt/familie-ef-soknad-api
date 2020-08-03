package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.mapper.tilDesimaltall
import no.nav.familie.ef.søknad.mapper.tilLocalDate
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Barnepass
import no.nav.familie.kontrakter.ef.søknad.BarnepassOrdning
import no.nav.familie.kontrakter.ef.søknad.Periode
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Barnepass as BarnepassDto

object BarnepassMapper {

    fun tilBarnepass(barnepass: BarnepassDto): Barnepass {
        return Barnepass(årsakBarnepass = barnepass.årsakBarnepass?.tilSøknadsfelt(),
                         barnepassordninger = Søknadsfelt("", barnepass.barnepassordninger.map { // TODO
                             BarnepassOrdning(hvaSlagsBarnepassOrdning = it.hvaSlagsBarnepassOrdning.tilSøknadsfelt(),
                                              navn = it.navn.tilSøknadsfelt(),
                                              periode = Søknadsfelt("", Periode(it.periode.fra.tilLocalDate().month, // TODO
                                                                                it.periode.fra.tilLocalDate().year,
                                                                                it.periode.til.tilLocalDate().month,
                                                                                it.periode.til.tilLocalDate().year)),
                                              belop = it.belop.tilSøknadsfelt(String::tilDesimaltall))
                         }))
    }
}