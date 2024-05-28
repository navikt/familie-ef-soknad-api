package no.nav.familie.ef.søknad.person.mapper

import no.nav.familie.ef.søknad.søknad.domain.AnnenForelder
import no.nav.familie.ef.søknad.søknad.domain.SamboerDetaljer
import no.nav.familie.ef.søknad.søknad.domain.TekstFelt
import no.nav.familie.ef.søknad.utils.Språktekster.AnnenForelderNavn
import no.nav.familie.ef.søknad.utils.Språktekster.IkkeOppgitt
import no.nav.familie.ef.søknad.utils.Språktekster.OmSamboer
import no.nav.familie.ef.søknad.utils.Språktekster.Persondata
import no.nav.familie.ef.søknad.utils.fødselsnummerTilTekstFelt
import no.nav.familie.ef.søknad.utils.hentTekst
import no.nav.familie.ef.søknad.utils.tilNullableDatoFelt
import no.nav.familie.ef.søknad.utils.tilSøknadsDatoFeltEllerNull
import no.nav.familie.ef.søknad.utils.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.PersonMinimum
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.kontrakter.felles.Fødselsnummer

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
        return PersonMinimum(
            annenForelder.navn?.tilSøknadsfelt() ?: Søknadsfelt(
                AnnenForelderNavn.hentTekst(),
                IkkeOppgitt.hentTekst(),
            ),
            søknadsfeltFødselsnummer,
            søknadsfeltFødselsdato,
            null,
        )
    }

    fun personMinimum(samboerDetaljer: SamboerDetaljer): PersonMinimum {
        val søknadsfeltFødselsnummer = mapFødselsnummer(samboerDetaljer.ident)
        val søknadsfeltFødselsdato = samboerDetaljer.fødselsdato?.tilSøknadsDatoFeltEllerNull()
        return PersonMinimum(
            samboerDetaljer.navn.tilSøknadsfelt(),
            søknadsfeltFødselsnummer,
            søknadsfeltFødselsdato,
            null,
        )
    }

    fun mapTilDto(personMinimum: PersonMinimum?): SamboerDetaljer? {
        if (personMinimum == null) return null
        return SamboerDetaljer(
            fødselsdato = personMinimum.fødselsdato.tilNullableDatoFelt(),
            navn = TekstFelt(personMinimum.navn.label, personMinimum.navn.verdi),
            ident = personMinimum.fødselsnummer.fødselsnummerTilTekstFelt(),
            kjennerIkkeIdent = personMinimum.fødselsnummer?.verdi?.verdi == null || personMinimum.fødselsnummer?.verdi?.verdi?.isBlank() == true,
        )
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
