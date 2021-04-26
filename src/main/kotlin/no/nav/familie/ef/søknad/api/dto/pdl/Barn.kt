package no.nav.familie.ef.søknad.api.dto.pdl

import java.time.LocalDate

data class Barn(val fnr: String,
                private val _navn: String,
                val alder: Int,
                val fødselsdato: LocalDate,
                val harSammeAdresse: Boolean,
                val medforelder: Medforelder?,
                val harAdressesperre: Boolean
) {

    val navn: String

    init {
        this.navn = when (harAdressesperre) {
            true -> ""
            false -> _navn
        }
    }

    override fun toString(): String {
        return "Barn(alder=$alder, fødselsdato=$fødselsdato, annenForelder=$medforelder, harAdressesperre=$harAdressesperre)"
    }


}


