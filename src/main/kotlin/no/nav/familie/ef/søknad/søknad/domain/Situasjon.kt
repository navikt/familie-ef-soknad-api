package no.nav.familie.ef.søknad.søknad.domain

data class Situasjon(
    val gjelderDetteDeg: ListFelt<String>,
    val sagtOppEllerRedusertStilling: TekstFelt? = null,
    val datoSagtOppEllerRedusertStilling: DatoFelt? = null,
    val begrunnelseSagtOppEllerRedusertStilling: TekstFelt? = null,
    val søknadsdato: DatoFelt?,
    val søkerFraBestemtMåned: BooleanFelt,
)
