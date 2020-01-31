package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Adresse(
    val adresse: String,
    val adressetillegg: String,
    val kommune: String,
    val postnummer: String
)