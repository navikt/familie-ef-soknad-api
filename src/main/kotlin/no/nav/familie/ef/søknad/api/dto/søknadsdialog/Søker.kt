package no.nav.familie.ef.søknad.api.dto.søknadsdialog

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Adresse

data class Søker(
        val adresse: Adresse? = null,
        val bankkontonummer: String? = null, // Trenger frontend dette?
        val egenansatt: Boolean? = null, // Frontend trenger ikke dette?
        val fnr: String,
        val forkortetNavn: String? = null, // Endre hva som returneres fra api - heller fornavn, (mellomnavn,) etternavn? Forkortet navn kan (logisk nok) føre til at man mister deler av navnet
        val innvandretDato: String? = null,// Trenger frontend dette?
        val jobbtelefon: String? = null,// Trenger frontend dette?
        val mobiltelefon: String? = null,
        val oppholdstillatelse: String? = null,// Trenger frontend dette?
        val privattelefon: String? = null,
        val sivilstand: String? = null,
        val språk: String? = null,
        val statsborgerskap: String? = null,
        val utvandretDato: String? = null// Trenger frontend dette?
)