package no.nav.familie.ef.søknad.minside.dto

data class SafDokumentOversiktRequest(
    val variables: SafDokumentVariables,
    val query: String
)

data class SafDokumentVariables(
    val ident: String
)
