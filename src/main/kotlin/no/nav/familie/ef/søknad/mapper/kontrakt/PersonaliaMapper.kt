package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Søker
import no.nav.familie.kontrakter.ef.søknad.Adresse
import no.nav.familie.kontrakter.ef.søknad.Fødselsnummer
import no.nav.familie.kontrakter.ef.søknad.Personalia
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import org.slf4j.LoggerFactory
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Adresse as AdresseDto

object PersonaliaMapper {

    private val secureLogger = LoggerFactory.getLogger("secureLogger")

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

    fun mapPersonalia(søker: Søker): Personalia {
        try {
            return Personalia(fødselsnummer = Søknadsfelt("Fødselsnummer", Fødselsnummer(søker.fnr)),
                              navn = Søknadsfelt("Navn", søker.forkortetNavn),
                              adresse = lagAdresseSøknadsFelt(søker.adresse),
                              statsborgerskap = Søknadsfelt("Statsborgerskap", søker.statsborgerskap),
                              telefonnummer = lagTelefonnummerSøknadsfelt(søker.kontakttelefon),
                              sivilstatus = Søknadsfelt("Sivilstatus", søker.sivilstand))
        } catch (e: Exception) {
            secureLogger.error("Feil ved mapping av person ${søker}")
            throw e
        }
    }
}
