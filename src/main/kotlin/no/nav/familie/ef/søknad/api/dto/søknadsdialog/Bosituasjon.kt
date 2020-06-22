package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Bosituasjon(
        val delerBoligMedAndreVoksne: TekstFelt,
        val datoFlyttetSammenMedSamboer: DatoFelt?,
        val samboerDetaljer: SamboerDetaljer?,
        val datoSkalGifteSegEllerBliSamboer: DatoFelt? = null,
        val skalGifteSegEllerBliSamboer: BooleanFelt?,
        val datoFlyttetFraHverandre: DatoFelt?
)

//delerBoligMedAndreVoksne: ISpørsmålBooleanFeltlFelt;
//datoFlyttetSammenMedSamboer?: IDatoFelt;
//samboerDetaljer?: IPersonDetaljer;
//datoSkalGifteSegEllerBliSamboer?: IDatoFelt;
//skalGifteSegEllerBliSamboer?: ISpørsmålBooleanFelt;
//datoFlyttetFraHverandre?: IDatoFelt;

data class SamboerDetaljer(
       // val fødselsnummer: String?,
        val fødselsdato: TekstFelt?,
        val navn: TekstFelt,
        val ident: TekstFelt? = null
)



