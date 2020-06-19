package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Dokumentasjonsbehov(val label: String,
                               val id: String,
                               val harSendtInn: BooleanFelt,
                               val opplastedeVedlegg: List<DokumentFelt> = emptyList())
