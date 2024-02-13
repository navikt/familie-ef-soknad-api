package no.nav.familie.ef.søknad.minside.dto

import java.time.LocalDate

data class StønadsperioderDto(
    val overgangsstønad: List<StønadsperiodeDto>,
    val barnetilsyn: List<StønadsperiodeDto>,
    val skolepenger: List<StønadsperiodeDto>,
)
data class StønadsperiodeDto(
    val fraDato: LocalDate,
    val tilDato: LocalDate,
    val beløp: Int,
)

data class MineStønaderDto(
    val overgangsstønad: Stønad,
    val barnetilsyn: Stønad,
    val skolepenger: Stønad,
)

data class Stønad(
    val periodeStatus: PeriodeStatus,
    val startDato: LocalDate?, // Startdato settes der hvor vi har fremtidige perioder uten opphold
    val sluttDato: LocalDate?, // Sluttdato settes der hvor vi har fremtidige eller løpende perioder uten opphold
    val perioder: List<StønadsperiodeDto>,
)

enum class PeriodeStatus {
    LØPENDE_UTEN_OPPHOLD,
    FREMTIDIG_UTEN_OPPHOLD,
    TIDLIGERE_ELLER_OPPHOLD,
    INGEN,
}
