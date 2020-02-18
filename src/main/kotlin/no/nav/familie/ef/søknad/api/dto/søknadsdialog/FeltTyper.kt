package no.nav.familie.ef.søknad.api.dto.søknadsdialog

import java.time.LocalDate

data class BooleanFelt(
        val label: String,
        val verdi: Boolean
)

data class TekstFelt(
        val label: String,
        val verdi: String
)

data class DatoFelt(
        val label: String,
        val verdi: LocalDate
)