package no.nav.familie.ef.søknad.api.dto.tps

import no.nav.familie.ef.søknad.integration.dto.pdl.Adressebeskyttelse
import no.nav.familie.ef.søknad.integration.dto.pdl.Dødsfall
import java.time.LocalDate

data class Barn(val fnr: String,
                val navn: String,
                val alder: Int,
                val fødselsdato: LocalDate,
                val harSammeAdresse: Boolean,
                val annenForelder: AnnenForelder?)


data class AnnenForelder(val navn: String?, val adressebeskyttelse: List<Adressebeskyttelse>, val dødsfall: List<Dødsfall>)
