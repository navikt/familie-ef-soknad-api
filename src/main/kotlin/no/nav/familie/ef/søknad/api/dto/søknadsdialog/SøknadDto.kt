package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class SøknadDto(
    val bekreftet: Boolean? = null,
    val bosituasjon: Bosituasjon? = null,
    val medlemskap: Medlemskap? = null,
    val person: Person,
    val sivilstatus: Sivilstatus? = null,
    val søkerBorPåRegistrertAdresse: BooleanFelt? = null,
    val vedleggsliste: List<VedleggFelt>? = null
)