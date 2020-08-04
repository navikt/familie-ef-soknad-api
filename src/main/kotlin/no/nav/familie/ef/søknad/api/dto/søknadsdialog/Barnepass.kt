package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Barnepass(val årsakBarnepass: TekstFelt?, // denne finnes i frontend men brukes ikke? Burde vi fjerne?
                     val barnepassordninger: List<BarnepassOrdning>)

data class BarnepassOrdning(val hvaSlagsBarnepassOrdning: TekstFelt,
                            val navn: TekstFelt,
                            val periode: Periode,
                            val belop: TekstFelt)
