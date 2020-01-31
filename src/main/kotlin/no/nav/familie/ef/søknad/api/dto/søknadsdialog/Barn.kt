package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Barn(
    val alder: Int,
    val fnr: String,
    val fødselsdato: String,
    val harSammeAdresse: Boolean,
    val navn: String
)