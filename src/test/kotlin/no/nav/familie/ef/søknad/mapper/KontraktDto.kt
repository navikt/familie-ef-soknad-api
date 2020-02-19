package no.nav.familie.ef.søknad.mapper

import no.nav.familie.kontrakter.ef.søknad.*
import java.time.LocalDate

/*
fun sivilstandsdetaljer(): Sivilstandsdetaljer {
    return Sivilstandsdetaljer(Søknadsfelt("Er du gift uten at dette er formelt registrert eller godkjent i Norge?",
                                           true),
                               SøknadMapper.dokumentfelt(
                                       "giftIUtlandetDokumentasjon"),
                               Søknadsfelt("Er du separert eller skilt uten at dette er formelt registrert eller godkjent i Norge?",
                                           true),
                               SøknadMapper.dokumentfelt(
                                       "separertEllerSkiltIUtlandetDokumentasjon"),
                               Søknadsfelt("Har dere søkt om separasjon, søkt om skilsmisse eller reist sak for domstolen?",
                                           true),
                               Søknadsfelt("Når søkte dere eller reiste sak?", LocalDate.of(2015, 12, 23)),
                               SøknadMapper.dokumentfelt(
                                       "Skilsmisse- eller separasjonsbevilling"),
                               Søknadsfelt("Hva er grunnen til at du er alene med barn?",
                                           "Endring i samværsordning"),
                               SøknadMapper.dokumentfelt(
                                       "Erklæring om samlivsbrudd"),
                               Søknadsfelt("Dato for samlivsbrudd", LocalDate.of(2014, 10, 3)),
                               Søknadsfelt("Når flyttet dere fra hverandre?", LocalDate.of(2014, 10, 4)),
                               Søknadsfelt("Spesifiser grunnen til at du er alene med barn?",
                                           "Trives best alene"),
                               Søknadsfelt("Når skjedde endringen / når skal endringen skje?",
                                           LocalDate.of(2013, 4, 17)))
}
*/

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

fun dokumentMap(): Map<String,Dokument> = mapOf( "samlivsbrudd" to Dokument("DOKUMENTID123".toByteArray(), "Erklæring om samlivsbrudd"))