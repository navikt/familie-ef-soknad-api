package no.nav.familie.ef.søknad.api.dto.søknadsdialog

import javax.validation.Valid

data class Person(val barn: List<Barn>? = null,
                  @field:Valid val søker: Søker)
