package no.nav.familie.ef.søknad.søknad.domain

import no.nav.familie.kontrakter.ef.iverksett.SvarId
import org.slf4j.LoggerFactory

fun String?.tilSvarIdOrNull(): SvarId? =
    try {
        this?.takeIf { it.isNotBlank() }?.let { enumValueOf<SvarId>(it) }
    } catch (e: IllegalArgumentException) {
        null // ugyldig enum-verdi => null
    }

fun TekstFelt?.tilSvarIdOrNull(): SvarId? = this?.svarid.tilSvarIdOrNull()

fun TekstFelt?.harGyldigSvarId(): Boolean? = this?.svarid.kanMappesTilSvarIdEnum()

fun String?.kanMappesTilSvarIdEnum(): Boolean? {
    if (this == null) return null
    try {
        enumValueOf<SvarId>(this)
    } catch (e: IllegalArgumentException) {
        return false
    }

    return true
}

fun BooleanFelt?.requireSvarIdIfPresent(): Boolean? = this?.svarid.kanMappesTilSvarIdEnum()
