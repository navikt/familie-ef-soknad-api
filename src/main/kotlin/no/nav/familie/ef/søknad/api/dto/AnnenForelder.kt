package no.nav.familie.ef.søknad.api.dto

import java.time.LocalDate

data class AnnenForelder(val fnr: String,
                         val fornavn: String,
                         val mellomnavn: String,
                         val etternavn: String,
                         val fødselsdato: LocalDate)
