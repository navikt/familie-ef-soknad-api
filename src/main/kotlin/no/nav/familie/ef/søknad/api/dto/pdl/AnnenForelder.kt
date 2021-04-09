package no.nav.familie.ef.søknad.api.dto.pdl

data class AnnenForelder(val navn: String, val harAdressesperre: Boolean, val død: Boolean = false)
