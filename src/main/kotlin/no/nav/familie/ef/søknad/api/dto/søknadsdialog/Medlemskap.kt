package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Medlemskap(
        val perioderBoddIUtlandet: List<PerioderBoddIUtlandet>?= listOf(),
        val søkerBosattINorgeSisteTreÅr: BooleanFelt,
        val søkerOppholderSegINorge: BooleanFelt
)

data class PerioderBoddIUtlandet(
        val begrunnelse: Begrunnelse,
        val periode: Periode
)

data class Begrunnelse(
        val label: String,
        val verdi: String
)

data class Periode(
        val fra: DatoFelt,
        val til: DatoFelt
)