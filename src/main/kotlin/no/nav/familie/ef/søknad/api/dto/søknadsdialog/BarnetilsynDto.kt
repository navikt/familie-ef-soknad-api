package no.nav.familie.ef.søknad.api.dto.søknadsdialog

import javax.validation.Valid

class BarnetilsynDto(
        @field:Valid val person: Person,
        val bosituasjon: Bosituasjon,

        val sivilstatus: Sivilstatus
)