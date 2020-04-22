package no.nav.familie.ef.søknad.api.dto.søknadsdialog


data class Barn(val alder: TekstFelt ,
                val fnr: TekstFelt,
                val fødselsdato: TekstFelt,
                val harSammeAdresse: BooleanFelt,
                val id: String? = null,
                val lagtTil: Boolean? = false,
                val navn: TekstFelt ,
                val personnummer: TekstFelt? = null,
                val født: BooleanFelt? = null,
                val skalBarnBoHosDeg: BooleanFelt? = null,
                val forelder: AnnenForelder?)

//IBARN
//id?: string;
//alder: ITekstFelt;
//fnr: ITekstFelt;
//fødselsdato: ITekstFelt;
//personnummer?: ITekstFelt;
//harSammeAdresse: IBooleanFelt;
//navn: ITekstFelt;
//født?: ISpørsmålBooleanFelt;
//lagtTil?: boolean;
//skalBarnBoHosDeg?: IBooleanFelt;
//forelder?: IForelder;

data class AnnenForelder (
    val    navn: TekstFelt?,
    val    skalBarnBoHosDeg: TekstFelt?,
    val    fødselsdato: DatoFelt?,
    val    personnr: TekstFelt?,
    val    borINorge: BooleanFelt?,
    val    avtaleOmDeltBosted: BooleanFelt?,
    val    harAnnenForelderSamværMedBarn: TekstFelt?,
    val    harDereSkriftligSamværsavtale: TekstFelt?,
    val    hvordanPraktiseresSamværet: TekstFelt?,
    val    borISammeHus: TekstFelt?,
    val    boddSammenFør: BooleanFelt?,
    val    flyttetFra: DatoFelt?,
    val    hvorMyeSammen: TekstFelt?
)

//export interface IForelder {
//    navn?: ITekstFelt;
//    skalBarnBoHosDeg?: ITekstFelt;
//    fødselsdato?: IDatoFelt | null;
//    personnr?: ITekstFelt;
//    borINorge?: IBooleanFelt;
//    avtaleOmDeltBosted?: ISpørsmålBooleanFelt;
//    harAnnenForelderSamværMedBarn?: ITekstFelt;
//    harDereSkriftligSamværsavtale?: ISpørsmålFelt;
//    hvordanPraktiseresSamværet?: ITekstFelt;
//    borISammeHus?: ITekstFelt;
//    boddSammenFør?: ISpørsmålBooleanFelt;
//    flyttetFra?: IDatoFelt;
//    hvorMyeSammen?: ITekstFelt;
//}