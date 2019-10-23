package no.nav.familie.ef.søknad.integration.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class RelasjonDto(val ident: String,
                       val forkortetNavn: String,
                       val alder: Int,
                       @JsonProperty("doedsdato") val dødsdato: DødsdatoDto?,
                       @JsonProperty("foedselsdato") val fødselsdato: LocalDate,
                       val harSammeAdresse: Boolean)
