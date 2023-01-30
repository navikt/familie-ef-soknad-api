package no.nav.familie.ef.søknad.api.dto.pdl

import no.nav.familie.kontrakter.felles.Fødselsnummer
import java.time.LocalDate
import java.time.Period

data class Medforelder(
    private val _navn: String,
    val harAdressesperre: Boolean,
    val død: Boolean = false,
    val ident: String,
) {

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

    override fun toString(): String {
        return "MedForelder(harAdressesperre=$harAdressesperre, død=$død, alder=$alder)"
    }
}
