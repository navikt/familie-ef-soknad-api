package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.BooleanFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.DatoFelt
import no.nav.familie.ef.søknad.mapper.Språktekster.FraMåned
import no.nav.familie.ef.søknad.mapper.Språktekster.FraÅr
import no.nav.familie.ef.søknad.mapper.Språktekster.NårSøkerDuFra
import no.nav.familie.ef.søknad.mapper.hentTekst
import no.nav.familie.ef.søknad.mapper.tilLocalDateEllerNull
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Stønadsstart
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt

object StønadsstartMapper {

    fun mapStønadsstart(
        søknadsdato: DatoFelt?,
        søkerFraBestemtMåned: BooleanFelt,
    ): Søknadsfelt<Stønadsstart> {
        return Søknadsfelt(NårSøkerDuFra.hentTekst(), map(søknadsdato, søkerFraBestemtMåned))
    }

    private fun map(
        søknadsdato: DatoFelt?,
        søkerFraBestemtMåned: BooleanFelt,
    ): Stønadsstart {
        val month = søknadsdato?.tilLocalDateEllerNull()?.month
        val year = søknadsdato?.tilLocalDateEllerNull()?.year
        return Stønadsstart(
            month?.let { Søknadsfelt(FraMåned.hentTekst(), month) },
            year?.let { Søknadsfelt(FraÅr.hentTekst(), year) },
            søkerFraBestemtMåned.tilSøknadsfelt(),
        )
    }
}
