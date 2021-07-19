package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Innsending(
    val beskrivelse: String,
    val dokumenttype: String,
    val vedlegg: no.nav.familie.kontrakter.ef.søknad.Dokument
)