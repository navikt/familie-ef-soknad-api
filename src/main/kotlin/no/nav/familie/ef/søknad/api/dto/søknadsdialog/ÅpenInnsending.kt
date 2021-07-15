package no.nav.familie.ef.søknad.api.dto.søknadsdialog

import no.nav.familie.kontrakter.ef.søknad.Vedlegg

data class ÅpenInnsending(val beskrivelse: String, val dokumenttype: String, val vedlegg: no.nav.familie.kontrakter.ef.søknad.Dokument)

//vedlegg: id, navn, tittel ??