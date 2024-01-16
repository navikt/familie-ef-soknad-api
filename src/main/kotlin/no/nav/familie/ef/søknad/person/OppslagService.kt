package no.nav.familie.ef.søknad.person

import no.nav.familie.ef.søknad.person.domain.Søkerinfo

interface OppslagService {

    fun hentSøkerinfo(): Søkerinfo

    fun hentSøkerNavn(): String
}
