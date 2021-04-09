package no.nav.familie.ef.søknad.api.dto.pdl

import java.time.LocalDate

data class Barn(val fnr: String,
                val navn: String,
                val alder: Int,
                val fødselsdato: LocalDate,
                val harSammeAdresse: Boolean,
                val annenForelder: AnnenForelder?)


