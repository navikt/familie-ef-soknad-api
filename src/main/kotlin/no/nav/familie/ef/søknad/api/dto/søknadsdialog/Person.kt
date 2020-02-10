package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Person(
        val barn: List<Barn>? = null,
        val søker: Søker
)