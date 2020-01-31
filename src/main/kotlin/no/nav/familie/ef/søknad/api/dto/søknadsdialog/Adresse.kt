package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Adresse(
    val adresse: String,
    val adressetillegg: String,// Trenger frontend dette?
    val kommune: String,// Trenger frontend dette?
    val postnummer: String
)