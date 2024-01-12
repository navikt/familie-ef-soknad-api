package no.nav.familie.ef.søknad.søknad.domain

data class Person(
    val barn: List<Barn>,
    val søker: Søker,
)

data class PersonTilGjenbruk(
    val barn: List<Barn>,
)
