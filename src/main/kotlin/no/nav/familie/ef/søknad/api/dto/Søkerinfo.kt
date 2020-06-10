package no.nav.familie.ef.søknad.api.dto

import no.nav.familie.ef.søknad.api.dto.tps.Barn
import no.nav.familie.ef.søknad.api.dto.tps.Person
import no.nav.familie.ef.søknad.util.lagDigest

data class Søkerinfo(val søker: Person,
                     val barn: List<Barn>) {

    val hash: String = lagDigest(this)

}
