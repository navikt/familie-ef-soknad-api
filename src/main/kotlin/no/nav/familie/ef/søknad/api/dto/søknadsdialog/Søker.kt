package no.nav.familie.ef.søknad.api.dto.søknadsdialog

import no.nav.familie.ef.søknad.validering.SjekkGyldigFødselsnummer

data class Søker(val adresse: Adresse,
                 val egenansatt: Boolean,
                 @field:SjekkGyldigFødselsnummer
                 val fnr: String,
                 val forkortetNavn: String,
                 val sivilstand: String,
                 val statsborgerskap: String,
                 val telefonnummer: String?)
