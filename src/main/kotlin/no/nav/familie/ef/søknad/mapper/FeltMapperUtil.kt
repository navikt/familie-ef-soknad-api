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

fun PeriodeFelt.tilSøknadsfelt(): Søknadsfelt<MånedÅrPeriode> =
        Søknadsfelt(this.label ?: error("Savner label"),
                    MånedÅrPeriode(this.fra.tilLocalDate().month,
                                   this.fra.tilLocalDate().year,
                                   this.til.tilLocalDate().month,
                                   this.til.tilLocalDate().year))

fun <T> ListFelt<T>.tilSøknadsfelt(): Søknadsfelt<List<T>> = Søknadsfelt(this.label, this.verdi, this.alternativer, this.svarid)

fun List<Dokumentasjonsbehov>.tilKontrakt(): List<DokumentasjonsbehovKontrakt> =
        this.map {
            DokumentasjonsbehovKontrakt(it.label,
                                        it.id,
                                        it.harSendtInn,
                                        it.opplastedeVedlegg.map { vedlegg -> Dokument(vedlegg.dokumentId, vedlegg.navn) })
        }

fun String.tilDesimaltall(): Double = this.replace(",", ".").toDouble()
fun String.tilHeltall(): Int = this.tilDesimaltall().toInt()

fun String.tilHeltallEllerTalletNull(): Int = when (this.isNotBlank()) {
    true -> this.tilDesimaltall().toInt()
    false -> 0
}


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
            Vedlegg(id = dokumentFelt.dokumentId,
                    navn = dokumentFelt.navn,
                    tittel = it.label)
        }
        val harSendtInn = Søknadsfelt(Språktekster.SendtInnTidligere.hentTekst(), it.harSendtInn)
        it.id to DokumentasjonWrapper(it.label, harSendtInn, vedlegg)
    }
}

