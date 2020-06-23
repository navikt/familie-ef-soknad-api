package no.nav.familie.ef.søknad.api.dto.søknadsdialog


data class Barn(val alder: TekstFelt?, // TODO Bare for visning (kan fjernes?)
                val ident: TekstFelt? = null, // TODO validation enten eller ident-eller termindato/fødselsdato?
                val fødselsdato: DatoFelt?,
                val harSammeAdresse: BooleanFelt,
                val ikkeRegistrertPåSøkersAdresseBeskrivelse: TekstFelt?,
                val id: String? = null,
                val lagtTil: Boolean? = false,
                val navn: TekstFelt?, // optional på nye barn - TODO - validere at denne er satt dersom lagtTil = false?
                val født: BooleanFelt,
                val forelder: AnnenForelder)

data class AnnenForelder(
        val kanIkkeOppgiAnnenForelderFar: BooleanFelt,
        val ikkeOppgittAnnenForelderBegrunnelse: TekstFelt?,
        val navn: TekstFelt?,
        val fødselsdato: DatoFelt?,
        val ident: TekstFelt? = null,
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


//    hvorforIkkeOppgi?: ISpørsmålFelt; // TODO brukes ikke
//TODO skal barn bo hos deg

