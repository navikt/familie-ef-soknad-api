package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.AnnenForelder
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SamboerDetaljer
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.TekstFelt
import no.nav.familie.ef.søknad.mapper.Språktekster.AnnenForelderNavn
import no.nav.familie.ef.søknad.mapper.Språktekster.IkkeOppgitt
import no.nav.familie.ef.søknad.mapper.Språktekster.OmSamboer
import no.nav.familie.ef.søknad.mapper.Språktekster.Persondata
import no.nav.familie.ef.søknad.mapper.hentTekst
import no.nav.familie.ef.søknad.mapper.tilSøknadsDatoFeltEllerNull
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Fødselsnummer
import no.nav.familie.kontrakter.ef.søknad.PersonMinimum
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt

object PersonMinimumMapper {

    fun map(samboerDetaljer: SamboerDetaljer): Søknadsfelt<PersonMinimum> {
        return Søknadsfelt(OmSamboer.hentTekst(), personMinimum(samboerDetaljer))
    }

    fun map(annenForelder: AnnenForelder): Søknadsfelt<PersonMinimum> {
        return Søknadsfelt(Persondata.hentTekst(), personMinimum(annenForelder))
    }

    private fun personMinimum(annenForelder: AnnenForelder): PersonMinimum {
        val søknadsfeltFødselsnummer = mapFødselsnummer(annenForelder.ident)
        val søknadsfeltFødselsdato = annenForelder.fødselsdato?.tilSøknadsDatoFeltEllerNull()
        return PersonMinimum(annenForelder.navn?.tilSøknadsfelt() ?: Søknadsfelt(AnnenForelderNavn.hentTekst(),
                                                                                 IkkeOppgitt.hentTekst()),
                             søknadsfeltFødselsnummer,
                             søknadsfeltFødselsdato,
                             null)
    }

    fun personMinimum(samboerDetaljer: SamboerDetaljer): PersonMinimum {
        val søknadsfeltFødselsnummer = mapFødselsnummer(samboerDetaljer.ident)
        val søknadsfeltFødselsdato = samboerDetaljer.fødselsdato?.tilSøknadsDatoFeltEllerNull()
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
