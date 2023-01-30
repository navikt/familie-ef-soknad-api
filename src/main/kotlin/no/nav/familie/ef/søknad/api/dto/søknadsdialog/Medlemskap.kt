package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Medlemskap(
    val perioderBoddIUtlandet: List<PerioderBoddIUtlandet>? = listOf(),
    val søkerBosattINorgeSisteTreÅr: BooleanFelt,
    val søkerOppholderSegINorge: BooleanFelt,
)

data class PerioderBoddIUtlandet(
    val begrunnelse: TekstFelt,
    val periode: PeriodeFelt,
)
