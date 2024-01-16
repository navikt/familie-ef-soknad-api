package no.nav.familie.ef.søknad.søknad.domain

data class Søker(
    val adresse: Adresse,
    val egenansatt: Boolean,
    val fnr: String,
    val forkortetNavn: String,
    val sivilstand: String,
    val statsborgerskap: String,
)
