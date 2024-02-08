package no.nav.familie.ef.søknad.person.domain

import java.time.LocalDate

data class Barn(
    val fnr: String,
    val navn: String,
    val alder: Int,
    val fødselsdato: LocalDate,
    val harSammeAdresse: Boolean,
    val medforelder: Medforelder?,
    val harAdressesperre: Boolean,
) {

    override fun toString(): String {
        return "Barn(alder=$alder, fødselsdato=$fødselsdato, annenForelder=$medforelder, harAdressesperre=$harAdressesperre)"
    }
}
