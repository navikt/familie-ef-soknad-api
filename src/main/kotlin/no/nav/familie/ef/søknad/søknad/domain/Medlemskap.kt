package no.nav.familie.ef.søknad.søknad.domain

data class Medlemskap(
    val utenlandsperiode: List<Utenlandsperiode>? = listOf(),
    val søkerBosattINorgeSisteTreÅr: BooleanFelt,
    val oppholdsland: TekstFelt? = null,
    val søkerOppholderSegINorge: BooleanFelt,
)

data class Utenlandsperiode(
    val begrunnelse: TekstFelt,
    val periode: PeriodeFelt,
    val land: TekstFelt? = null,
    val personident: TekstFelt? = null,
    val adresse: TekstFelt? = null,
)
