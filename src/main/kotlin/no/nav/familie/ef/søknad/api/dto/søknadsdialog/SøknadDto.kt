package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class SøknadDto(
        val begrunnelseForSøknad: String? = null,
        val bekreftet: Boolean? = null,
        val person: Person,
        val søkerBorPåRegistrertAdresse: Boolean? = null,
        val søkerBosattINorgeSisteTreÅr: Boolean? = null,
        val søkerOppholderSegINorge: Boolean? = null,
        val vedleggsliste: List<Any>? = null
)