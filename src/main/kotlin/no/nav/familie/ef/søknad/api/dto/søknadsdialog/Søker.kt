package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Søker(
        val adresse: Adresse,
        val egenansatt: Boolean? = null,
        val fnr: String,
        val forkortetNavn: String ,
        val sivilstand: String, // TODO endre til sivilstatus?
        val statsborgerskap: String,
        val telefonnummer: String?
)