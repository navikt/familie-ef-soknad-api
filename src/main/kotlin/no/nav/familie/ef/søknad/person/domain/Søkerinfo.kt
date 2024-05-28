package no.nav.familie.ef.søknad.person.domain

data class Søkerinfo(
    val søker: Person,
    val barn: List<Barn>,
) {
    val hash: String = this.hashCode().toString()
}
