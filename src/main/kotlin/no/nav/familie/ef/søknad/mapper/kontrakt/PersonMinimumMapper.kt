package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.AnnenForelder
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SamboerDetaljer
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.TekstFelt
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Fødselsnummer
import no.nav.familie.kontrakter.ef.søknad.PersonMinimum
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt

object PersonMinimumMapper {
    fun map(it: SamboerDetaljer): Søknadsfelt<PersonMinimum> {
        return Søknadsfelt("Om samboeren din", personMinimum(it))
    }

    fun map(annenForelder: AnnenForelder): Søknadsfelt<PersonMinimum> {
        return Søknadsfelt("Persondata", personMinimum(annenForelder))
    }

    private fun personMinimum(annenForelder: AnnenForelder): PersonMinimum {

        val søknadsfeltFødselsnummer =  mapFødselsnummer(annenForelder.ident)

        val søknadsfeltFødselsdato = annenForelder.fødselsdato?.tilSøknadsfelt()

        return PersonMinimum(annenForelder.navn?.tilSøknadsfelt()  ?: Søknadsfelt("Annen forelder navn",
                                                                                  "ikke oppgitt") ,
                             søknadsfeltFødselsnummer,
                             søknadsfeltFødselsdato,
                             null)

    }


    private fun personMinimum(samboerDetaljer: SamboerDetaljer): PersonMinimum {

        val søknadsfeltFødselsnummer =  mapFødselsnummer(samboerDetaljer.ident)

        val søknadsfeltFødselsdato = samboerDetaljer.fødselsdato?.tilSøknadsfelt()

        return PersonMinimum(samboerDetaljer.navn.tilSøknadsfelt(),
                             søknadsfeltFødselsnummer,
                             søknadsfeltFødselsdato,
                             null)

    }

    private fun mapFødselsnummer(ident: TekstFelt?): Søknadsfelt<Fødselsnummer>? {
        return ident?.let {
            return if (it.verdi.isNotBlank()) {
                ident.tilSøknadsfelt(::Fødselsnummer)
            } else {
                null
            }
        }

    }


}