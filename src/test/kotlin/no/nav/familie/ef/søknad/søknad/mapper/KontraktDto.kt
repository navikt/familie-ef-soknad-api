package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.utils.DokumentasjonWrapper
import no.nav.familie.kontrakter.ef.søknad.Adresse
import no.nav.familie.kontrakter.ef.søknad.Personalia
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Vedlegg
import no.nav.familie.kontrakter.felles.Fødselsnummer

fun personalia(): Personalia =
    Personalia(
        Søknadsfelt("Fødselsnummer", Fødselsnummer("19128449828")),
        Søknadsfelt("Navn", "Kari Nordmann"),
        Søknadsfelt("Statsborgerskap", "NOR"),
        adresseSøknadsfelt(),
        Søknadsfelt("Sivilstatus", "UGIF"),
    )

fun adresseSøknadsfelt(): Søknadsfelt<Adresse> =
    Søknadsfelt(
        "Adresse",
        Adresse(
            "Jerpefaret 5C",
            "1440",
            "",
            "",
        ),
    )

fun dokumentMap(): Map<String, DokumentasjonWrapper> {
    val vedlegg = Vedlegg("id", "navn", "tittel")
    return mapOf(
        "samlivsbrudd" to DokumentasjonWrapper("label", Søknadsfelt("Har allerede sendt inn", false), listOf(vedlegg)),
        "TERMINBEKREFTELSE" to DokumentasjonWrapper("Terminbekreftelse", Søknadsfelt("Har allerede sendt inn", false), listOf(vedlegg)),
    )
}
