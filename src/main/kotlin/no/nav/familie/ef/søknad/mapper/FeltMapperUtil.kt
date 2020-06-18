package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.*
import no.nav.familie.kontrakter.ef.søknad.Dokument
import no.nav.familie.kontrakter.ef.søknad.Dokumentasjon
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import java.time.LocalDate

fun BooleanFelt.tilSøknadsfelt(): Søknadsfelt<Boolean> = Søknadsfelt(this.label, this.verdi)

fun TekstFelt.tilSøknadsfelt(): Søknadsfelt<String> = Søknadsfelt(this.label, this.verdi)
fun HeltallFelt.tilSøknadsfelt(): Søknadsfelt<Int> = Søknadsfelt(this.label, this.verdi)
fun <T> TekstFelt.tilSøknadsfelt(t: (String) -> T): Søknadsfelt<T> = Søknadsfelt(this.label, t.invoke(this.verdi))

fun DatoFelt.tilSøknadsfelt(): Søknadsfelt<LocalDate> = Søknadsfelt(this.label, this.verdi)

fun <T> ListFelt<T>.tilSøknadsfelt(): Søknadsfelt<List<T>> = Søknadsfelt(this.label, this.verdi)

fun dokumentfelt(dokumentnavn: String, vedleggMap: Map<String, DokumentasjonWrapper>): Søknadsfelt<Dokumentasjon>? {
    val dokumentasjon = vedleggMap[dokumentnavn]
    return dokumentasjon?.let {
        val dokumenter = it.vedlegg.map { vedlegg -> Dokument(vedlegg.id, vedlegg.navn) }
        Søknadsfelt(it.label, Dokumentasjon(it.harSendtInnTidligere, dokumenter))
    }
}


fun String.tilHeltall(): Int = this.toDouble().toInt()
