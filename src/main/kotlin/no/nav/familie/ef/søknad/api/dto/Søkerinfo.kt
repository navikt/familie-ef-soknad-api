package no.nav.familie.ef.søknad.api.dto

import no.nav.familie.ef.søknad.api.dto.pdl.Barn
import no.nav.familie.ef.søknad.api.dto.pdl.Person

data class Søkerinfo(
    val søker: Person,
    val barn: List<Barn>,
) {

    val hash: String = this.hashCode().toString()
}
