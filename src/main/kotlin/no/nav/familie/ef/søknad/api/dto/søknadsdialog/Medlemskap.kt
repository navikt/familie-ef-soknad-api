package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Medlemskap(
    val søkerBosattINorgeSisteTreÅr: BooleanFelt? = null,
    val søkerOppholderSegINorge: BooleanFelt? = null
)