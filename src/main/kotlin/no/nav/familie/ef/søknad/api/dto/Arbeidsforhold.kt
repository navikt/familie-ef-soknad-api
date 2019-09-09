package no.nav.familie.ef.søknad.api.dto

import java.time.LocalDate

class Arbeidsforhold(val arbeidsgiverId: String,
                     val arbeidsgiverIdType: String,
                     val arbeidsgiverNavn: String,
                     val stillingsprosent: Double,
                     val fom: LocalDate,
                     val tom: LocalDate?)
