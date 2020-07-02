package no.nav.familie.ef.søknad.mapper

import no.nav.familie.kontrakter.ef.søknad.*


fun personalia(): Personalia {
    return Personalia(Søknadsfelt("Fødselsnummer", Fødselsnummer("19128449828")),
                      Søknadsfelt("Navn", "Kari Nordmann"),
                      Søknadsfelt("Statsborgerskap", "NOR"),
                      adresseSøknadsfelt(),
                      Søknadsfelt("Telefonnummer", "99988877"),
                      Søknadsfelt("Sivilstatus", "UGIF"))
}

fun adresseSøknadsfelt(): Søknadsfelt<Adresse> {
    return Søknadsfelt("Adresse",
                       Adresse("Jerpefaret 5C",
                               "1440",
                               "",
                               ""))
}

fun dokumentMap(): Map<String, DokumentasjonWrapper> {
    val vedlegg = Vedlegg("id", "navn", "tittel", "data".toByteArray())
    return mapOf("samlivsbrudd" to DokumentasjonWrapper("label", Søknadsfelt("Har allerede sendt inn", false), listOf(vedlegg)))
}
