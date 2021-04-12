package no.nav.familie.ef.søknad.api.dto.pdl

import no.nav.familie.kontrakter.ef.søknad.Fødselsnummer
import java.time.LocalDate
import java.time.Period

class AnnenForelder(_navn: String, val harAdressesperre: Boolean, val død: Boolean = false, val ident: String) {

    val navn: String
    val alder: Int

    init {
        val fødselsnummer = Fødselsnummer(ident)
        this.alder = Period.between(fødselsnummer.fødselsdato, LocalDate.now()).years
        this.navn = when (harAdressesperre) {
            true -> "Person $alder år"
            false -> _navn
        }

    }

}
