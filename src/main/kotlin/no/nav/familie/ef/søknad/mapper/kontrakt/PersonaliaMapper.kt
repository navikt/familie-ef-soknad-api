package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.kontrakter.ef.søknad.Adresse
import no.nav.familie.kontrakter.ef.søknad.Fødselsnummer
import no.nav.familie.kontrakter.ef.søknad.Personalia
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt

object PersonaliaMapper {

    fun mapPersonalia(frontendDto: SøknadDto): Personalia =
            Personalia(
                    fødselsnummer = lagFødselsnummerSøknadsFelt(frontendDto),
                    navn = lagNavnSøknadsFelt(frontendDto),
                    adresse = lagAdresseSøknadsFelt(frontendDto),
                    statsborgerskap = Søknadsfelt("Statsborgerskap", frontendDto.person.søker.statsborgerskap),
                    telefonnummer = lagTelefonnummerSøknadsfelt(frontendDto.person.søker.kontakttelefon),
                    sivilstatus = Søknadsfelt("Sivilstatus", frontendDto.person.søker.sivilstand)
            )

    private fun lagTelefonnummerSøknadsfelt(telefonnummer: String?): Søknadsfelt<String>? {
        return telefonnummer?.let {
            Søknadsfelt("Telefonnummer", it)
        }
    }

    private fun lagAdresseSøknadsFelt(frontendDto: SøknadDto): Søknadsfelt<Adresse> {
        val frontendAdresse = frontendDto.person.søker.adresse
        return Søknadsfelt("Adresse",
                           Adresse(adresse = frontendAdresse.adresse,
                                   postnummer = frontendAdresse.postnummer,
                                   poststedsnavn = "",
                                   land = "")) // TODO endre når kodeverk integrasjon er ferdig
    }

    private fun lagNavnSøknadsFelt(frontendDto: SøknadDto): Søknadsfelt<String> =
            Søknadsfelt("Navn", frontendDto.person.søker.forkortetNavn)


    private fun lagFødselsnummerSøknadsFelt(frontendDto: SøknadDto): Søknadsfelt<Fødselsnummer> {
        return Søknadsfelt("Fødselsnummer", Fødselsnummer(frontendDto.person.søker.fnr))
    }
}
