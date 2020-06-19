package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Situasjon(val datoOppstartJobb: DatoFelt? = null,
                     val datoOppstartUtdanning: DatoFelt? = null,
                     val gjelderDetteDeg: ListFelt<String>,
                     val sagtOppEllerRedusertStilling: TekstFelt? = null,
                     val datoSagtOppEllerRedusertStilling: DatoFelt? = null,
                     val begrunnelseSagtOppEllerRedusertStilling: TekstFelt? = null,
                     val søknadsdato: DatoFelt?,
                     val søkerFraBestemtMåned: BooleanFelt)

//export interface IDinSituasjon {
//    gjelderDetteDeg: ISpørsmålListeFelt;
//    datoOppstartJobb?: IDatoFelt;
//    datoOppstartUtdanning?: IDatoFelt;
//    søknadsdato?: IDatoFelt;
//    sagtOppEllerRedusertStilling?: ISpørsmålFelt;
//    begrunnelseSagtOppEllerRedusertStilling?: ITekstFelt;
//    datoSagtOppEllerRedusertStilling?: IDatoFelt;
//    søkerFraBestemtMåned?: ISpørsmålFelt;
//}