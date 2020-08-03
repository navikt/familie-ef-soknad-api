package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.BooleanFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.DatoFelt
import no.nav.familie.ef.søknad.mapper.tilLocalDateEllerNull
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Stønadsstart
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt

object StønadsstartMapper {

    fun mapStønadsstart(søknadsdato: DatoFelt?,
                        søkerFraBestemtMåned: BooleanFelt): Stønadsstart {
        val month = søknadsdato?.tilLocalDateEllerNull()?.month
        val year = søknadsdato?.tilLocalDateEllerNull()?.year
        return Stønadsstart(month?.let { Søknadsfelt("Fra måned", month) },
                            year?.let { Søknadsfelt("Fra år", year) },
                            søkerFraBestemtMåned.tilSøknadsfelt())
    }

}
