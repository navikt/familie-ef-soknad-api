package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Søkerinfo

interface OppslagService {

    fun hentSøkerinfo(): Søkerinfo

    fun hentSøkerinfoV2(): Søkerinfo

}
