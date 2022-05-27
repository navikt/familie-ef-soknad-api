package no.nav.familie.ef.søknad.api.dto.pdl

data class Person(
    val fnr: String,
    val forkortetNavn: String,
    val adresse: Adresse,
    val egenansatt: Boolean,
    val sivilstand: String,
    val statsborgerskap: String,
    val harAdressesperre: Boolean
)
