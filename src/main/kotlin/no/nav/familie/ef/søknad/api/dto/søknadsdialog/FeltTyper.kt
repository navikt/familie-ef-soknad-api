package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class BooleanFelt(val label: String,
                       val verdi: Boolean)

data class TekstFelt(val label: String,
                     val verdi: String)

data class DatoFelt(val label: String,
                    val verdi: String)

data class DokumentFelt(val dokumentId: String,
                        val navn: String)

data class ListFelt<T>(val label: String,
                       val verdi: List<T>,
                       val alternativer: List<String>? = null)

data class HeltallFelt(val label: String,
                       val verdi: Int)

data class PeriodeFelt(val label: String?, // Hvis den ikke finnes med i alle ennå så er den optional
                       val fra: DatoFelt,
                       val til: DatoFelt)