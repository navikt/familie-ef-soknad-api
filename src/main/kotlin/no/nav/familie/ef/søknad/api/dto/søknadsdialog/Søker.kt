package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Søker(
        val adresse: Adresse? = null,
        val egenansatt: Boolean? = null,
        val fnr: String,
        val forkortetNavn: String? = null,
        val sivilstand: String? = null,
        val statsborgerskap: String? = null
)