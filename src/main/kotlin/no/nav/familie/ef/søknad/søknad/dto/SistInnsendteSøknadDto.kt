package no.nav.familie.ef.søknad.søknad.dto

import java.time.LocalDate

data class SistInnsendteSøknadDto(
    val søknadsdato: LocalDate,
    val søknadType: String,
)
