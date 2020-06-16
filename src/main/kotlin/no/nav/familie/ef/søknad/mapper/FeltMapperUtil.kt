package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.*
import no.nav.familie.kontrakter.ef.søknad.Dokument
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import java.time.LocalDate

fun BooleanFelt.tilSøknadsfelt(): Søknadsfelt<Boolean> = Søknadsfelt(this.label, this.verdi)

fun TekstFelt.tilSøknadsfelt(): Søknadsfelt<String> = Søknadsfelt(this.label, this.verdi)
fun HeltallFelt.tilSøknadsfelt(): Søknadsfelt<Int> = Søknadsfelt(this.label, this.verdi)
fun <T> TekstFelt.tilSøknadsfelt(t: (String) -> T): Søknadsfelt<T> = Søknadsfelt(this.label, t.invoke(this.verdi))

fun DatoFelt.tilSøknadsfelt(): Søknadsfelt<LocalDate> = Søknadsfelt(this.label, this.verdi)

fun <T> ListFelt<T>.tilSøknadsfelt(): Søknadsfelt<List<T>> = Søknadsfelt(this.label, this.verdi)

fun dokumentfelt(dokumentNavn: String, alleDokumenter: Map<String, List<Dokument>>): Søknadsfelt<List<Dokument>>? {
    val dokumenter = alleDokumenter[dokumentNavn]
    return dokumenter?.let {
        return if(dokumenter.isEmpty()) {
            Søknadsfelt(dokumentNavn, emptyList())
        } else {
            Søknadsfelt(dokumenter.first().tittel, dokumenter)
        }
    }
}

fun String.tilHeltall(): Int = this.toDouble().toInt()
