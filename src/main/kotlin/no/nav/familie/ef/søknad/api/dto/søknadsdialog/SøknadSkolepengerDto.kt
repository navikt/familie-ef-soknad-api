package no.nav.familie.ef.søknad.api.dto.søknadsdialog

import javax.validation.Valid

class SøknadSkolepengerDto(
        val bosituasjon: Bosituasjon,
        val medlemskap: Medlemskap,
        @field:Valid val person: Person,
        val sivilstatus: Sivilstatus,
        val søkerBorPåRegistrertAdresse: BooleanFelt?,
        val utdanning: UnderUtdanning,
        val dokumentasjonsbehov: List<Dokumentasjonsbehov>
)
