package no.nav.familie.ef.søknad.api.dto.pdl

import no.nav.familie.ef.søknad.integration.dto.pdl.Adressebeskyttelse
import no.nav.familie.ef.søknad.integration.dto.pdl.Dødsfall

data class AnnenForelder(val navn: String?, val adressebeskyttelse: List<Adressebeskyttelse>, val dødsfall: List<Dødsfall>)
