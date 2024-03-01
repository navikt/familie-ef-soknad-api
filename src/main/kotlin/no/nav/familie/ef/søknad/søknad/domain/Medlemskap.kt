package no.nav.familie.ef.søknad.søknad.domain

data class Medlemskap(
    val perioderBoddIUtlandet: List<PerioderBoddIUtlandet>? = listOf(),
    val søkerBosattINorgeSisteTreÅr: BooleanFelt,
    val oppholdsland: TekstFelt? = null,
    val søkerOppholderSegINorge: BooleanFelt,
)

data class PerioderBoddIUtlandet(
    val begrunnelse: TekstFelt,
    val periode: PeriodeFelt,
    val land: TekstFelt? = null,
    val personidentUtland: TekstFelt? = null,
    val adresseUtland: TekstFelt? = null,
)
