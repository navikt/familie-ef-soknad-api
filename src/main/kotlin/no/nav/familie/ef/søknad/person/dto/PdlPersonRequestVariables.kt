package no.nav.familie.ef.søknad.person.dto

data class PdlPersonRequestVariables(
    var ident: String
)

data class PdlPersonBolkRequestVariables(
    var identer: List<String>
)
