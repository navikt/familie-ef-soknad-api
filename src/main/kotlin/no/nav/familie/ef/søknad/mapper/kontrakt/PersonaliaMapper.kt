package no.nav.familie.ef.søknad.mapper.kontrakt


import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.kontrakter.ef.søknad.*

object PersonaliaMapper {

    fun personalia(): Personalia {
        return Personalia(Søknadsfelt("Fødselsnummer", Fødselsnummer("24117938529")),
                          Søknadsfelt("Navn", "Kari Nordmann"),
                          Søknadsfelt("Statsborgerskap", "Norsk"),
                          adresseSøknadsfelt(),
                          Søknadsfelt("Telefonnummer", "12345678"),
                          Søknadsfelt("Sivilstand", "Ugift"))
    }

    private fun adresseSøknadsfelt(): Søknadsfelt<Adresse> {
        return Søknadsfelt("Adresse",
                           Adresse("Jerpefaret 5C",
                                   "1440",
                                   "Drøbak",
                                   "Norge"))
    }

    fun mapPersonalia(frontendDto: SøknadDto): Søknadsfelt<Personalia> {
        val personalia = personalia()
                .copy(fødselsnummer = lagFødselsnummerSøknadsFelt(
                        frontendDto),
                      navn = lagNavnSøknadsFelt(
                              frontendDto),
                      adresse = lagAdresseSøknadsFelt(
                              frontendDto),
                      statsborgerskap = Søknadsfelt("Statsborgerskap",
                                                                         frontendDto.person.søker.statsborgerskap),
                      telefonnummer = lagTelefonnummerSøknadsfelt(
                              frontendDto.person.søker.telefonnummer),
                      sivilstatus = Søknadsfelt("Sivilstatus", frontendDto.person.søker.sivilstand)

        )
        return Søknadsfelt("personalia", personalia)
    }

    private fun lagTelefonnummerSøknadsfelt(telefonnummer: String?): Søknadsfelt<String>? {
        return telefonnummer?.let {
            Søknadsfelt("Telefonnummer", telefonnummer)
        }
    }

    private fun lagAdresseSøknadsFelt(frontendDto: SøknadDto): Søknadsfelt<Adresse> {
        val frontendAdresse = frontendDto.person.søker.adresse
        return Søknadsfelt("Adresse", Adresse(adresse = frontendAdresse.adresse, postnummer = frontendAdresse.postnummer))
    }

    private fun lagNavnSøknadsFelt(frontendDto: SøknadDto): Søknadsfelt<String> =
            Søknadsfelt("Navn", frontendDto.person.søker.forkortetNavn)


    private fun lagFødselsnummerSøknadsFelt(frontendDto: SøknadDto): Søknadsfelt<Fødselsnummer> {
        return Søknadsfelt("Fødselsnummer", Fødselsnummer(frontendDto.person.søker.fnr))
    }

}
