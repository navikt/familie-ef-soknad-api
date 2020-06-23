package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.*
import no.nav.familie.kontrakter.ef.søknad.Dokument
import no.nav.familie.kontrakter.ef.søknad.Dokumentasjon
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Vedlegg
import java.time.*
import java.time.format.DateTimeFormatter


fun BooleanFelt.tilSøknadsfelt(): Søknadsfelt<Boolean> = Søknadsfelt(this.label, this.verdi)

fun TekstFelt.tilSøknadsfelt(): Søknadsfelt<String> = Søknadsfelt(this.label, this.verdi)
fun HeltallFelt.tilSøknadsfelt(): Søknadsfelt<Int> = Søknadsfelt(this.label, this.verdi)
fun <T> TekstFelt.tilSøknadsfelt(t: (String) -> T): Søknadsfelt<T> = Søknadsfelt(this.label, t.invoke(this.verdi))

fun <T> ListFelt<T>.tilSøknadsfelt(): Søknadsfelt<List<T>> = Søknadsfelt(this.label, this.verdi)

fun dokumentfelt(dokumentnavn: String, vedleggMap: Map<String, DokumentasjonWrapper>): Søknadsfelt<Dokumentasjon>? {
    val dokumentasjon = vedleggMap[dokumentnavn]
    return dokumentasjon?.let {
        val dokumenter = it.vedlegg.map { vedlegg -> Dokument(vedlegg.id, vedlegg.navn) }
        Søknadsfelt(it.label, Dokumentasjon(it.harSendtInnTidligere, dokumenter))
    }
}

data class DokumentasjonWrapper(val label: String, val harSendtInnTidligere: Søknadsfelt<Boolean>, val vedlegg: List<Vedlegg>)

fun String.tilHeltall(): Int = this.toDouble().toInt()

fun DatoFelt.tilSøknadsDatoFeltEllerNull(): Søknadsfelt<LocalDate>? {
    return if (this.verdi.isNotBlank()) {
        Søknadsfelt(this.label, fraStrengTilLocalDate(verdi))
    } else {
        null
    }
}

fun DatoFelt.tilSøknadsfelt(): Søknadsfelt<LocalDate> {
    return if (this.verdi.isNotBlank()) {
        Søknadsfelt(this.label, fraStrengTilLocalDate(this.verdi))
    } else {
        throw IllegalArgumentException("Kan ikke mappe datoFelt sin verdi når den er tom for ${this.label}")
    }
}

fun DatoFelt.tilLocalDate(): LocalDate {
    return if (this.verdi.isNotBlank()) {
        fraStrengTilLocalDate(this.verdi)
    } else {
        throw IllegalArgumentException("Kan ikke mappe datoFelt sin verdi når den er tom for ${this.label}")
    }
}

fun DatoFelt.tilLocalDateEllerNull(): LocalDate? {
    return if (this.verdi.isNotBlank()) {
        fraStrengTilLocalDate(this.verdi)
    } else {
        null
    }
}

private fun fraStrengTilLocalDate(verdi: String): LocalDate {
    return if (verdi.length > 10 && verdi[10] == 'T') {
        if (verdi.endsWith("Z")) {
            val offsetDateTime = OffsetDateTime.ofInstant(Instant.parse(verdi), ZoneId.of("Europe/Oslo"))
            offsetDateTime.toLocalDate()
        } else {
            LocalDateTime.parse(verdi, DateTimeFormatter.ISO_LOCAL_DATE_TIME).toLocalDate()
        }
    } else {
        LocalDate.parse(verdi, DateTimeFormatter.ISO_LOCAL_DATE)
    }
}



