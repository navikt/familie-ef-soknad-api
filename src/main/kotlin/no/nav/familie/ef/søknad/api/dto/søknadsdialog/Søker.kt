package no.nav.familie.ef.søknad.api.dto.søknadsdialog

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Adresse

data class Søker(
        val adresse: Adresse? = null,
        val bankkontonummer: String? = null,
        val egenansatt: Boolean? = null,
        val fnr: String,
        val forkortetNavn: String? = null,
        val innvandretDato: String? = null,
        val jobbtelefon: String? = null,
        val mobiltelefon: String? = null,
        val oppholdstillatelse: String? = null,
        val privattelefon: String? = null,
        val sivilstand: String? = null,
        val språk: String? = null,
        val statsborgerskap: String? = null,
        val utvandretDato: String? = null
)