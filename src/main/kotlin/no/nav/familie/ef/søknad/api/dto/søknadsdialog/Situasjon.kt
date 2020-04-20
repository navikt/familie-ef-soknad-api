package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Situasjon(
        val datoOppstartJobb: DatoFelt? = null,
        val datoOppstartUtdanning: DatoFelt? = null,
        val gjelderDetteDeg: GjelderDetteDeg,
        val sagtOppEllerRedusertStilling: TekstFelt? = null,
        val datoSagtOppEllerRedusertStilling: DatoFelt? = null,
        val begrunnelseSagtOppEllerRedusertStilling: TekstFelt? = null,
        val søknadsdato: DatoFelt
)

data class GjelderDetteDeg(
        val label: String,
        val verdi: List<String>
)
