package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Søker
import no.nav.familie.ef.søknad.mapper.Mapper
import no.nav.familie.ef.søknad.mapper.Språktekster.*
import no.nav.familie.ef.søknad.mapper.hentTekst
import no.nav.familie.kontrakter.ef.søknad.Adresse
import no.nav.familie.kontrakter.ef.søknad.Fødselsnummer
import no.nav.familie.kontrakter.ef.søknad.Personalia
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Adresse as AdresseDto
import no.nav.familie.ef.søknad.mapper.Språktekster.Adresse as AdresseTekst
import no.nav.familie.ef.søknad.mapper.Språktekster.Fødselsnummer as FødselsnummerTekst
import no.nav.familie.ef.søknad.mapper.Språktekster.Søker as SpråkTeksterSøker

object PersonaliaMapper : Mapper<Søker, Personalia>(SpråkTeksterSøker) {


    override fun mapDto(søker: Søker): Personalia {


        return Personalia(fødselsnummer = Søknadsfelt(FødselsnummerTekst.hentTekst(), Fødselsnummer(søker.fnr)),
                          navn = Søknadsfelt(Navn.hentTekst(), søker.forkortetNavn),
                          adresse = lagAdresseSøknadsFelt(søker.adresse),
                          statsborgerskap = Søknadsfelt(Statsborgerskap.hentTekst(), søker.statsborgerskap),
                          telefonnummer = lagTelefonnummerSøknadsfelt(søker.kontakttelefon),
                          sivilstatus = Søknadsfelt(Sivilstatus.hentTekst(), søker.sivilstand))
    }


    private fun lagTelefonnummerSøknadsfelt(telefonnummer: String?): Søknadsfelt<String>? {
        return telefonnummer?.let {
            Søknadsfelt(Telefonnummer.hentTekst(), it)
        }
    }

    private fun lagAdresseSøknadsFelt(frontendAdresse: AdresseDto): Søknadsfelt<Adresse> {
        return Søknadsfelt(AdresseTekst.hentTekst(),
                           Adresse(adresse = frontendAdresse.adresse,
                                   postnummer = frontendAdresse.postnummer,
                                   poststedsnavn = frontendAdresse.poststed ?: "",
                                   land = ""))
    }
}
