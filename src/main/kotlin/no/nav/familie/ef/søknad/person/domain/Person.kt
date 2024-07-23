package no.nav.familie.ef.søknad.person.domain

data class Person(
    val fnr: String,
    val alder: Int,
    val forkortetNavn: String,
    val adresse: Adresse,
    val egenansatt: Boolean,
    val sivilstand: String,
    val statsborgerskap: String,
    val erStrengtFortrolig: Boolean,
)
