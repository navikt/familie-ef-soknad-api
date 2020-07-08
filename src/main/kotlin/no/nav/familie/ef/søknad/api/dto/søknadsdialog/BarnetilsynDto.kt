package no.nav.familie.ef.søknad.api.dto.søknadsdialog

import javax.validation.Valid

class BarnetilsynDto(
        @field:Valid val person: Person,
        val dokumentasjonsbehov: List<Dokumentasjonsbehov>
)