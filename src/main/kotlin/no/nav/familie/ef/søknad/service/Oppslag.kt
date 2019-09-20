package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.AktørId
import no.nav.familie.ef.søknad.api.dto.Person
import no.nav.familie.ef.søknad.api.dto.Søkerinfo

interface Oppslag {

    fun hentPerson(): Person

    fun hentSøkerinfo(): Søkerinfo

    fun hentAktørId(fnr: String): AktørId

}
