package no.nav.familie.ef.søknad.api.dto.pdl

import no.nav.familie.kontrakter.ef.søknad.Fødselsnummer
import java.time.LocalDate
import java.time.Period
import java.util.Objects

class Medforelder(_navn: String, val harAdressesperre: Boolean, val død: Boolean = false, val ident: String) {

    val navn: String
    val alder: Int

    init {
        val fødselsnummer = Fødselsnummer(ident)
        this.alder = Period.between(fødselsnummer.fødselsdato, LocalDate.now()).years
        this.navn = when (harAdressesperre) {
            true -> ""
            false -> _navn
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Medforelder

        if (harAdressesperre != other.harAdressesperre) return false
        if (død != other.død) return false
        if (ident != other.ident) return false
        if (navn != other.navn) return false
        if (alder != other.alder) return false

        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(ident.hashCode(),
                            alder,
                            harAdressesperre.hashCode(),
                            navn.hashCode(),
                            død.hashCode()
        )
    }

    override fun toString(): String {
        return "MedForelder(harAdressesperre=$harAdressesperre, død=$død, alder=$alder)"
    }


}
