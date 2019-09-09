package no.nav.familie.ef.søknad.integration.dto

import no.nav.familie.ef.søknad.api.dto.Bankkonto
import no.nav.familie.ef.søknad.api.dto.Barn

import java.time.LocalDate

data class PersonDto(val fnr: String,
                     val aktorId: String,
                     val fornavn: String,
                     val mellomnavn: String,
                     val etternavn: String,
                     val kjønn: String,
                     val fødselsdato: LocalDate,
                     val målform: String,
                     val bankkonto: Bankkonto,
                     val barn: List<Barn>)
