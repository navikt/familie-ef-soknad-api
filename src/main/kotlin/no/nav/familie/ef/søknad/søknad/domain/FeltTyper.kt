package no.nav.familie.ef.søknad.søknad.domain

data class BooleanFelt(
    val label: String,
    val verdi: Boolean,
    val svarid: String? = null,
)

data class TekstFelt(
    val label: String,
    val verdi: String,
    val svarid: String? = null,
)

data class DatoFelt(
    val label: String,
    val verdi: String,
)

data class DokumentFelt(
    val dokumentId: String,
    val navn: String,
)

data class ListFelt<T>(
    val label: String,
    val verdi: List<T>,
    val alternativer: List<String>? = null,
    val svarid: List<T>? = null,
)

data class HeltallFelt(
    val label: String,
    val verdi: Int,
)

data class PeriodeFelt(
    val label: String?, // Hvis den ikke finnes med i alle ennå så er den optional
    val fra: DatoFelt,
    val til: DatoFelt,
)
