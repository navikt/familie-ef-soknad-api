package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class SøknadDto(val bekreftet: Boolean? = null,
                     val bosituasjon: Bosituasjon,
                     val medlemskap: Medlemskap,
                     val person: Person,
                     val sivilstatus: Sivilstatus,
                     val søkerBorPåRegistrertAdresse: BooleanFelt? = null,
                     val vedleggsliste: List<VedleggFelt>)
