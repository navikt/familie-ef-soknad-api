package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.BooleanFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.DatoFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Dokumentasjonsbehov
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.HeltallFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.ListFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.PeriodeFelt
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.TekstFelt
import no.nav.familie.kontrakter.ef.søknad.Dokument
import no.nav.familie.kontrakter.ef.søknad.MånedÅrPeriode
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
import no.nav.familie.kontrakter.ef.søknad.Vedlegg
import no.nav.familie.kontrakter.felles.Fødselsnummer
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import no.nav.familie.kontrakter.ef.søknad.Dokumentasjonsbehov as DokumentasjonsbehovKontrakt

fun BooleanFelt.tilSøknadsfelt(): Søknadsfelt<Boolean> = Søknadsfelt(this.label, this.verdi)

fun HeltallFelt.tilSøknadsfelt(): Søknadsfelt<Int> = Søknadsfelt(this.label, this.verdi)

fun TekstFelt.tilSøknadsfelt(): Søknadsfelt<String> = Søknadsfelt(label = this.label, verdi = this.verdi, svarId = this.svarid)
fun <T> TekstFelt.tilSøknadsfelt(t: (String) -> T): Søknadsfelt<T> = Søknadsfelt(label = this.label, verdi = t.invoke(this.verdi))

fun Søknadsfelt<Fødselsnummer>?.fødselsnummerTilTekstFelt(): TekstFelt? = this?.let { TekstFelt(it.label, it.verdi.verdi, it.svarId?.verdi) }

fun Søknadsfelt<LocalDate>?.tilDatoFelt() = this?.let { DatoFelt(it.label, it.verdi.toString()) }

fun Søknadsfelt<Boolean>?.tilBooleanFelt() = this?.let { BooleanFelt(it.label, it.verdi, it.svarId.toString()) }
fun Søknadsfelt<String>?.tilTekstFelt() = this?.let { TekstFelt(it.label, it.verdi, it.svarId) }

fun <T> Søknadsfelt<T>?.tilNullableDatoFelt() = this?.let { DatoFelt(it.label, it.verdi.toString()) }
fun <T>Søknadsfelt<T>.tilDatoFelt() = DatoFelt(this.label, this.verdi.toString())
fun Søknadsfelt<Boolean>?.tilNullableBooleanFelt() = this?.let { BooleanFelt(it.label, it.verdi, it.svarId.toString()) }
fun Søknadsfelt<Boolean>.tilBooleanFelt() = BooleanFelt(this.label, this.verdi, this.svarId.toString())
fun <T> Søknadsfelt<T>?.tilNullableTekstFelt() = this?.let { TekstFelt(it.label, it.verdi.toString(), it.svarId.toString()) }

fun <T> Søknadsfelt<T>.tilTekstFelt() = TekstFelt(this.label, this.verdi.toString(), this.svarId.toString())

fun Søknadsfelt<List<String>>.tilListFelt() = ListFelt(label, verdi.map { it }, alternativer, svarId)
fun PeriodeFelt.tilSøknadsfelt(): Søknadsfelt<MånedÅrPeriode> =
    Søknadsfelt(
        this.label ?: error("Savner label"),
        MånedÅrPeriode(
            this.fra.tilLocalDate().month,
            this.fra.tilLocalDate().year,
            this.til.tilLocalDate().month,
            this.til.tilLocalDate().year,
        ),
    )

fun <T> ListFelt<T>.tilSøknadsfelt(): Søknadsfelt<List<T>> = Søknadsfelt(this.label, this.verdi, this.alternativer, this.svarid)

fun List<Dokumentasjonsbehov>.tilKontrakt(): List<DokumentasjonsbehovKontrakt> =
    this.map {
        DokumentasjonsbehovKontrakt(
            it.label,
            it.id,
            it.harSendtInn,
            it.opplastedeVedlegg.map { vedlegg -> Dokument(vedlegg.dokumentId, vedlegg.navn) },
        )
    }

fun String.tilDesimaltall(): Double = this.replace(",", ".").toDouble()
fun String.tilHeltall(): Int = this.tilDesimaltall().toInt()

fun DatoFelt.tilSøknadsDatoFeltEllerNull(): Søknadsfelt<LocalDate>? {
    return if (this.verdi.isNotBlank()) {
        Søknadsfelt(this.label, fraStrengTilLocalDate(verdi))
    } else {
        null
    }
}

fun DatoFelt.tilSøknadsfelt(): Søknadsfelt<LocalDate> {
    require(this.verdi.isNotBlank()) { "Kan ikke mappe datoFelt sin verdi når den er tom for ${this.label}" }
    return Søknadsfelt(this.label, fraStrengTilLocalDate(this.verdi))
}

fun DatoFelt.tilLocalDate(): LocalDate {
    require(this.verdi.isNotBlank()) { "Kan ikke mappe datoFelt sin verdi når den er tom for ${this.label}" }
    return fraStrengTilLocalDate(this.verdi)
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

fun lagDokumentasjonWrapper(dokumentasjonsbehov: List<Dokumentasjonsbehov>): Map<String, DokumentasjonWrapper> {
    return dokumentasjonsbehov.associate {
        // it.id er dokumenttype/tittel, eks "Gift i utlandet"
        val vedlegg = it.opplastedeVedlegg.map { dokumentFelt ->
            Vedlegg(
                id = dokumentFelt.dokumentId,
                navn = dokumentFelt.navn,
                tittel = it.label,
            )
        }
        val harSendtInn = Søknadsfelt(Språktekster.SendtInnTidligere.hentTekst(), it.harSendtInn)
        it.id to DokumentasjonWrapper(it.label, harSendtInn, vedlegg)
    }
}
