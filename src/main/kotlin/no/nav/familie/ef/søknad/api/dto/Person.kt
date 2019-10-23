package no.nav.familie.ef.søknad.api.dto

import java.time.LocalDate

data class Person(val fnr: String,
                  val forkortetNavn: String,
                  val adresse: Adresse,
                  val egenansatt: Boolean,
                  val innvandretDato: LocalDate?,
                  val utvandretDato: LocalDate?,
                  val oppholdstillatelse: String,
                  val sivilstand: String,
                  val språk: String,
                  val statsborgerskap: String,
                  val privattelefon: String,
                  val mobiltelefon: String,
                  val jobbtelefon: String,
                  val bankkontonummer: String)
