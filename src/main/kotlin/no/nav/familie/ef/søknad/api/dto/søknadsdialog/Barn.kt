package no.nav.familie.ef.søknad.api.dto.søknadsdialog


data class Barn(val alder: TekstFelt,
                val fnr: TekstFelt? = null,
                val fødselsdato: DatoFelt,
                val harSammeAdresse: BooleanFelt,
                val ikkeRegistrertPåSøkersAdresseBeskrivelse: TekstFelt?,
                val id: String? = null,
                val lagtTil: Boolean? = false,
                val navn: TekstFelt,
                val personnummer: TekstFelt? = null,
                val født: BooleanFelt,
                val forelder: AnnenForelder)

data class AnnenForelder(
        val kanIkkeOppgiAnnenForelderFar: BooleanFelt,
        val ikkeOppgittAnnenForelderBegrunnelse: TekstFelt?,
        val navn: TekstFelt?,
        val fødselsdato: DatoFelt?,
        val personnr: TekstFelt?,
        val borINorge: BooleanFelt?,
        val land: TekstFelt?,
        val avtaleOmDeltBosted: BooleanFelt?,
        val harAnnenForelderSamværMedBarn: TekstFelt?,
        val harDereSkriftligSamværsavtale: TekstFelt?,
        val hvordanPraktiseresSamværet: TekstFelt?,
        val borAnnenForelderISammeHus: TekstFelt?,
        val borAnnenForelderISammeHusBeskrivelse: TekstFelt?,
        val boddSammenFør: BooleanFelt?,
        val flyttetFra: DatoFelt?,
        val hvorMyeSammen: TekstFelt?,
        val beskrivSamværUtenBarn: TekstFelt?,
        val skalBarnetBoHosSøker: TekstFelt?
)

//
//export interface IBarn {
//    id: string;
//    alder: ITekstFelt;
//    fnr: ITekstFelt;
//    fødselsdato: ITekstFelt;
//    personnummer?: ITekstFelt;
//    harSammeAdresse: IBooleanFelt;
//    navn: ITekstFelt;
//    født?: ISpørsmålBooleanFelt;
//    lagtTil?: boolean;
//    forelder?: IForelder;
//}

//
//export interface IForelder {
//    navn?: ITekstFelt;
//    skalBarnetBoHosSøker?: ISpørsmålFelt;
//    fødselsdato?: IDatoFelt | null;
//    personnr?: ITekstFelt;
//    kanIkkeOppgiAnnenForelderFar?: IBooleanFelt;
//    hvorforIkkeOppgi?: ISpørsmålFelt; // TODO brukes ikke
//    ikkeOppgittAnnenForelderBegrunnelse?: ITekstFelt;
//    borINorge?: ISpørsmålBooleanFelt;
//    land?: ITekstFelt;
//    avtaleOmDeltBosted?: ISpørsmålBooleanFelt;
//    harAnnenForelderSamværMedBarn?: ISpørsmålFelt;
//    harDereSkriftligSamværsavtale?: ISpørsmålFelt;
//    hvordanPraktiseresSamværet?: ITekstFelt;
//    borISammeHus?: ISpørsmålFelt;
//    hvordanBorDere?: ISpørsmålFelt;
//    boddSammenFør?: ISpørsmålBooleanFelt;
//    flyttetFra?: IDatoFelt;
//    hvorMyeSammen?: ISpørsmålFelt;
//    beskrivSamværUtenBarn?: ITekstFelt;
//}

//TODO skal barn bo hos deg

