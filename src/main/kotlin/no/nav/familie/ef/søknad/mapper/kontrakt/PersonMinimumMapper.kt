package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SamboerDetaljer
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Fødselsnummer
import no.nav.familie.kontrakter.ef.søknad.PersonMinimum
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt

object PersonMinimumMapper {
    fun map(it: SamboerDetaljer): Søknadsfelt<PersonMinimum> {
        return Søknadsfelt("Om samboeren din", personMinimum(it))
    }


    private fun personMinimum(samboerDetaljer: SamboerDetaljer): PersonMinimum {

        val søknadsfeltFødselsnummer = samboerDetaljer.fødselsnummer?.let {
            Søknadsfelt("Fødselsnummer", Fødselsnummer(it))
        }

        val søknadsfeltFødselsdato = samboerDetaljer.fødselsdato?.tilSøknadsfelt()

        return PersonMinimum(Søknadsfelt("Navn", samboerDetaljer.navn),
                             søknadsfeltFødselsnummer,
                             søknadsfeltFødselsdato,
                             null)

    }
}