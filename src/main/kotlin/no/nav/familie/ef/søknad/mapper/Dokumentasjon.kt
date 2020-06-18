package no.nav.familie.ef.søknad.mapper

import no.nav.familie.kontrakter.ef.søknad.Vedlegg

data class DokumentasjonWrapper(val label: String, val harSendtInnTidligere: Boolean, val vedlegg: List<Vedlegg>)