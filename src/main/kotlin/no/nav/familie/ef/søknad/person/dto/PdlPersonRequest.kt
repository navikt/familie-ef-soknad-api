package no.nav.familie.ef.søknad.person.dto

data class PdlPersonRequest(
    val variables: PdlPersonRequestVariables,
    val query: String
)

data class PdlPersonBolkRequest(
    val variables: PdlPersonBolkRequestVariables,
    val query: String
)
