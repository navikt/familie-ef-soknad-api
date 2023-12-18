package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Person(
    val barn: List<Barn>,
    val søker: Søker,
)

data class PersonTilGjenbruk(
    val barn: List<Barn>,
)
