package no.nav.familie.ef.søknad.api.dto.pdl

import java.time.LocalDate
import java.util.Objects

class Barn(val fnr: String,
           _navn: String,
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


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Barn

        if (fnr != other.fnr) return false
        if (alder != other.alder) return false
        if (fødselsdato != other.fødselsdato) return false
        if (harSammeAdresse != other.harSammeAdresse) return false
        if (medforelder != other.medforelder) return false
        if (harAdressesperre != other.harAdressesperre) return false
        if (navn != other.navn) return false

        return true
    }


    override fun hashCode(): Int {

        return Objects.hash(fnr,
                            alder,
                            fødselsdato.hashCode(),
                            harAdressesperre.hashCode(),
                            medforelder?.hashCode() ?: 0,
                            navn.hashCode())

    }

    override fun toString(): String {
        return "Barn(alder=$alder, fødselsdato=$fødselsdato, annenForelder=$medforelder, harAdressesperre=$harAdressesperre)"
    }


}


