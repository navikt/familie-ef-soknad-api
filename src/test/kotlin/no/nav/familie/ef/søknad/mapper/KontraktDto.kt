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

fun dokumentMap(): Map<String, List<Vedlegg>> {
    val vedlegg = Vedlegg("id", "navn", "tittel", "data".toByteArray())
    return mapOf("samlivsbrudd" to listOf(vedlegg))
}
