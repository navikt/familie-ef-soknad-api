package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Barnepass(val årsakBarnepass: TekstFelt?,
                     val barnepassordninger: List<BarnepassOrdning>)

data class BarnepassOrdning(val hvaSlagsBarnepassOrdning: TekstFelt,
                            val navn: TekstFelt,
                            val periode: PeriodeFelt,
                            val belop: TekstFelt)
