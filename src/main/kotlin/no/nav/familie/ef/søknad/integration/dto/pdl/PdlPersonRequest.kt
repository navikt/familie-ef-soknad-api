package no.nav.familie.ef.sak.integration.dto.pdl

data class PdlPersonRequest(val variables: PdlPersonRequestVariables,
                            val query: String)

data class PdlPersonBolkRequest(val variables: PdlPersonBolkRequestVariables,
                                val query: String)
