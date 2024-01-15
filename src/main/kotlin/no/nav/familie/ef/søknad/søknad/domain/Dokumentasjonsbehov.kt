package no.nav.familie.ef.søknad.søknad.domain

data class Dokumentasjonsbehov(
    val label: String,
    val id: String,
    val harSendtInn: Boolean,
    val opplastedeVedlegg: List<DokumentFelt> = emptyList(),
)
