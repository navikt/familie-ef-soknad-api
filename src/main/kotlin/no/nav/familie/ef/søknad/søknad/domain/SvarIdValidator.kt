package no.nav.familie.ef.søknad.søknad.domain

import no.nav.familie.kontrakter.ef.iverksett.SvarId
import org.slf4j.LoggerFactory

// === Mapper fra String til enum SvarId? ===
fun String?.tilSvarIdOrNull(): SvarId? =
    try {
        this?.takeIf { it.isNotBlank() }?.let { enumValueOf<SvarId>(it) }
    } catch (e: IllegalArgumentException) {
        null // ugyldig enum-verdi => null
    }

fun String?.requireSvarId(feltNavn: String): SvarId? =
    this?.takeIf { it.isNotBlank() }?.let {
        SvarId.valueOf(it)
    }

// === Extension-funksjoner for felttyper ===

fun TekstFelt?.tilSvarIdOrNull(): SvarId? = this?.svarid.tilSvarIdOrNull()

fun TekstFelt?.harGyldigSvarId(): Boolean = this?.svarid.kanMappesTilSvarIdEnum(this?.label ?: "ukjent TekstFelt")

fun String?.kanMappesTilSvarIdEnum(feltNavn: String): Boolean {
    if (this == null) return true
    try {
        enumValueOf<SvarId>(this)
    } catch (e: IllegalArgumentException) {
        return false
    }

    return true
}

fun BooleanFelt?.requireSvarIdIfPresent(): Boolean? = this?.svarid.kanMappesTilSvarIdEnum(this?.label ?: "ukjent BooleanFelt")
