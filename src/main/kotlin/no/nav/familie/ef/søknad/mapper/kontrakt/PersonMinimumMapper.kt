package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.AnnenForelder
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SamboerDetaljer
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.TekstFelt
import no.nav.familie.ef.søknad.mapper.tilSøknadsDatoFeltEllerNull
import no.nav.familie.ef.søknad.mapper.tilSøknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Fødselsnummer
import no.nav.familie.kontrakter.ef.søknad.PersonMinimum
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import org.slf4j.LoggerFactory

object PersonMinimumMapper {

    private val secureLogger = LoggerFactory.getLogger("secureLogger")

    fun map(samboerDetaljer: SamboerDetaljer): Søknadsfelt<PersonMinimum> {
        try {
            return Søknadsfelt("Om samboeren din", personMinimum(samboerDetaljer))
        } catch (e: Exception) {
            secureLogger.error("Feil ved mapping av samboerdetaljer: $samboerDetaljer")
            throw e
        }
    }

    fun map(annenForelder: AnnenForelder): Søknadsfelt<PersonMinimum> {
        try {
            return Søknadsfelt("Persondata", personMinimum(annenForelder))
        } catch (e: Exception) {
            secureLogger.error("Feil ved mapping av annen forelder: $annenForelder")
            throw e
        }
    }

    private fun personMinimum(annenForelder: AnnenForelder): PersonMinimum {
        val søknadsfeltFødselsnummer = mapFødselsnummer(annenForelder.ident)
        val søknadsfeltFødselsdato = annenForelder.fødselsdato?.tilSøknadsDatoFeltEllerNull()
        return PersonMinimum(annenForelder.navn?.tilSøknadsfelt() ?: Søknadsfelt("Annen forelder navn",
                                                                                 "ikke oppgitt"),
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
