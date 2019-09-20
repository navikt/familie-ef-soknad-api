package no.nav.familie.ef.søknad.api.dto

import java.time.LocalDate

class Barn(val fornavn: String,
           val mellomnavn: String,
           val etternavn: String,
           val fnr: String,
           val kjønn: String,
           val fødselsdato: LocalDate,
           val annenForelder: AnnenForelder)
