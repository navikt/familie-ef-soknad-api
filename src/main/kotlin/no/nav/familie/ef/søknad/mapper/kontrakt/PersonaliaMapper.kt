package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Søker
import no.nav.familie.ef.søknad.mapper.Mapper
import no.nav.familie.kontrakter.ef.søknad.Adresse
import no.nav.familie.kontrakter.ef.søknad.Fødselsnummer
import no.nav.familie.kontrakter.ef.søknad.Personalia
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Adresse as AdresseDto

object PersonaliaMapper : Mapper<Søker, Personalia>("Søker") {


    override fun mapDto(søker: Søker): Personalia {
        return Personalia(fødselsnummer = Søknadsfelt("Fødselsnummer", Fødselsnummer(søker.fnr)),
                          navn = Søknadsfelt("Navn", søker.forkortetNavn),
                          adresse = lagAdresseSøknadsFelt(søker.adresse),
                          statsborgerskap = Søknadsfelt("Statsborgerskap", søker.statsborgerskap),
                          telefonnummer = lagTelefonnummerSøknadsfelt(søker.kontakttelefon),
                          sivilstatus = Søknadsfelt("Sivilstatus", søker.sivilstand))
    }

    private fun lagTelefonnummerSøknadsfelt(telefonnummer: String?): Søknadsfelt<String>? {
        return telefonnummer?.let {
            Søknadsfelt("Telefonnummer", it)
        }
    }

    private fun lagAdresseSøknadsFelt(frontendAdresse: AdresseDto): Søknadsfelt<Adresse> {
        return Søknadsfelt("Adresse",
                           Adresse(adresse = frontendAdresse.adresse,
                                   postnummer = frontendAdresse.postnummer,
                                   poststedsnavn = frontendAdresse.poststed ?: "",
                                   land = ""))
    }
}
