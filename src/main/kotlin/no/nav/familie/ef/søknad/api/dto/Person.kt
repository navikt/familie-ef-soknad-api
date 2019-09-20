package no.nav.familie.ef.søknad.api.dto

import java.time.LocalDate

class Person(val fnr: String,
             val fornavn: String,
             val mellomnavn: String,
             val etternavn: String,
             val kjønn: String,
             val fødselsdato: LocalDate,
             val bankkonto: Bankkonto,
             val barn: List<Barn>)
