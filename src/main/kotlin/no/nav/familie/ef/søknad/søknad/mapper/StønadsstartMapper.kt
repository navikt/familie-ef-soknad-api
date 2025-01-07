package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.søknad.domain.BooleanFelt
import no.nav.familie.ef.søknad.søknad.domain.DatoFelt
import no.nav.familie.ef.søknad.utils.Språktekster.FraMåned
import no.nav.familie.ef.søknad.utils.Språktekster.FraÅr
import no.nav.familie.ef.søknad.utils.Språktekster.NårSøkerDuFra
import no.nav.familie.ef.søknad.utils.hentTekst
import no.nav.familie.ef.søknad.utils.tilLocalDateEllerNull
import no.nav.familie.ef.søknad.utils.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Stønadsstart
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt

object StønadsstartMapper {
    fun mapStønadsstart(
        søknadsdato: DatoFelt?,
        søkerFraBestemtMåned: BooleanFelt,
    ): Søknadsfelt<Stønadsstart> = Søknadsfelt(NårSøkerDuFra.hentTekst(), map(søknadsdato, søkerFraBestemtMåned))

    private fun map(
        søknadsdato: DatoFelt?,
        søkerFraBestemtMåned: BooleanFelt,
    ): Stønadsstart {
        val month = søknadsdato?.tilLocalDateEllerNull()?.month
        val year = søknadsdato?.tilLocalDateEllerNull()?.year
        return Stønadsstart(
            fraMåned = month?.let { Søknadsfelt(FraMåned.hentTekst(), month) },
            fraÅr = year?.let { Søknadsfelt(FraÅr.hentTekst(), year) },
            søkerFraBestemtMåned = søkerFraBestemtMåned.tilSøknadsfelt(),
        )
    }
}
