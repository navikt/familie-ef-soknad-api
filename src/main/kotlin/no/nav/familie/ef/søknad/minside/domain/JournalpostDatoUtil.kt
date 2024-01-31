package no.nav.familie.ef.søknad.minside.domain

import java.time.LocalDateTime

object JournalpostDatoUtil {

    fun mestRelevanteDato(journalpost: Journalpost): LocalDateTime? {
        return journalpost.relevanteDatoer.maxByOrNull { datoTyperSortert(it.datotype) }?.dato
    }

    private fun datoTyperSortert(datoType: String) = when (datoType) {
        "DATO_REGISTRERT" -> 4
        "DATO_JOURNALFOERT" -> 3
        "DATO_DOKUMENT" -> 2
        "DATO_OPPRETTET" -> 1
        else -> 0
    }
}
