package no.nav.familie.ef.søknad.mapper

import no.nav.familie.kontrakter.ef.søknad.*


fun personalia(): Personalia {
    return Personalia(Søknadsfelt("Fødselsnummer", Fødselsnummer("19128449828")),
                      Søknadsfelt("Navn", "Kari Nordmann"),
                      Søknadsfelt("Statsborgerskap", "NOR"),
                      adresseSøknadsfelt(),
                      null,
                      Søknadsfelt("Sivilstatus", "UGIF"))
}

fun adresseSøknadsfelt(): Søknadsfelt<Adresse> {
    return Søknadsfelt("Adresse",
                       Adresse("Jerpefaret 5C",
                               "1440",
                               null,
                               null))
}

fun dokumentMap(): Map<String, List<Dokument>> = mapOf("samlivsbrudd" to listOf(Dokument("DOKUMENTID123".toByteArray(),
                                                                            "Erklæring om samlivsbrudd")))
