package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Situasjon(val gjelderDetteDeg: ListFelt<String>,
                     val sagtOppEllerRedusertStilling: TekstFelt? = null,
                     val datoSagtOppEllerRedusertStilling: DatoFelt? = null,
                     val begrunnelseSagtOppEllerRedusertStilling: TekstFelt? = null,
                     val søknadsdato: DatoFelt?,
                     val søkerFraBestemtMåned: BooleanFelt)
