package no.nav.familie.ef.søknad.api.dto.tps

data class Person(val fnr: String,
                  val forkortetNavn: String,
                  val adresse: Adresse,
                  val egenansatt: Boolean,
                  val sivilstand: String,
                  val statsborgerskap: String)
