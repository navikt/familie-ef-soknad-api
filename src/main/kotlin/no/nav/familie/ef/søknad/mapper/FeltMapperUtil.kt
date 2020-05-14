package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.BooleanFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.DatoFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.ListFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.TekstFelt
import no.nav.familie.kontrakter.ef.søknad.Dokument
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import java.time.LocalDate

fun BooleanFelt.tilSøknadsfelt(): Søknadsfelt<Boolean> = Søknadsfelt(this.label, this.verdi)

fun TekstFelt.tilSøknadsfelt(): Søknadsfelt<String> = Søknadsfelt(this.label, this.verdi)
fun <T> TekstFelt.tilSøknadsfelt(t: (String) -> T): Søknadsfelt<T> = Søknadsfelt(this.label, t.invoke(this.verdi))

fun DatoFelt.tilSøknadsfelt(): Søknadsfelt<LocalDate> = Søknadsfelt(this.label, this.verdi)

fun <T> ListFelt<T>.tilSøknadsfelt(): Søknadsfelt<List<T>> = Søknadsfelt(this.label, this.verdi)

fun falseOrNull(it: Boolean?) = it ?: false

fun dokumentfelt(dokumentNavn: String, dokumenter: Map<String, Dokument>): Søknadsfelt<Dokument>? {
    val dokument = dokumenter[dokumentNavn]
    return dokument?.let {
        Søknadsfelt(dokument.tittel, dokument)
    }
}