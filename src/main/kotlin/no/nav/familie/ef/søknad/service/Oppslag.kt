package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Søkerinfo

interface Oppslag {

    fun hentSøkerinfo(): Søkerinfo
    fun hentPoststedFor(postnummer: String): String

}
