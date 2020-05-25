package no.nav.familie.ef.søknad.mapper

import no.nav.familie.kontrakter.ef.søknad.*
import java.time.LocalDate

fun sivilstandsdetaljer(): Sivilstandsdetaljer {
    return Sivilstandsdetaljer(null,
                               null,
                               null,
                               null,
                               null,
                               null,
                               null,
                               null,
                               Søknadsfelt("Erklæring om samlivsbrudd", Dokument(byteArrayOf(12), "Erklæring om samlivsbrudd")),
                               Søknadsfelt("Dato for samlivsbrudd", LocalDate.of(2014, 10, 3)),
                               null,
                               null,
                               null)
}


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

fun dokumentMap(): Map<String, Dokument> = mapOf("samlivsbrudd" to Dokument("DOKUMENTID123".toByteArray(),
                                                                            "Erklæring om samlivsbrudd"))
