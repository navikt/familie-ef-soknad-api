package no.nav.familie.ef.søknad.søknad.domain

data class Medlemskap(
    val perioderBoddIUtlandet: List<Utenlandsperiode>? = listOf(),
    val søkerBosattINorgeSisteTreÅr: BooleanFelt,
    val oppholdsland: TekstFelt? = null,
    val søkerOppholderSegINorge: BooleanFelt,
)

data class Utenlandsperiode(
    val begrunnelse: TekstFelt,
    val periode: PeriodeFelt,
    val land: TekstFelt? = null,
    val personidentEøsLand: TekstFelt? = null,
    val adresseEøsLand: TekstFelt? = null,
    val erEøsLand: Boolean? = null,
    val kanIkkeOppgiPersonident: BooleanFelt? = null,
)
