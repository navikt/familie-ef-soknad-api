package no.nav.familie.ef.søknad.api.dto.søknadsdialog

import javax.validation.Valid

data class SøknadDto(val bekreftet: Boolean? = null,
                     val bosituasjon: Bosituasjon,
                     val medlemskap: Medlemskap,
                     @field:Valid val person: Person,
                     val sivilstatus: Sivilstatus,
                     val søkerBorPåRegistrertAdresse: BooleanFelt? = null,
                     val vedleggsliste: List<VedleggFelt>,
                     val aktivitet: Aktivitet,
                     val merOmDinSituasjon: Situasjon)


