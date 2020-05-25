package no.nav.familie.ef.søknad.api.dto.søknadsdialog


data class Barn(val alder: TekstFelt,
                val fnr: TekstFelt,
                val fødselsdato: DatoFelt,
                val harSammeAdresse: BooleanFelt,
                val ikkeRegistrertPåSøkersAdresseBeskrivelse: TekstFelt?,
                val id: String? = null,
                val lagtTil: Boolean? = false,
                val navn: TekstFelt,
                val personnummer: TekstFelt? = null,
                val født: BooleanFelt,
                val skalBarnBoHosDeg: BooleanFelt? = null,
                val forelder: AnnenForelder)

data class AnnenForelder(
        val kanIkkeOppgiAnnenForelderFar: BooleanFelt,
        val ikkeOppgittAnnenForelderBegrunnelse: TekstFelt?,
        val navn: TekstFelt,
        val fødselsdato: DatoFelt?,
        val personnr: TekstFelt?,
        val borINorge: BooleanFelt?,
        val land: TekstFelt?,
        val avtaleOmDeltBosted: BooleanFelt,
        val harAnnenForelderSamværMedBarn: TekstFelt?,
        val harDereSkriftligSamværsavtale: TekstFelt?,
        val hvordanPraktiseresSamværet: TekstFelt?,
        val borAnnenForelderISammeHus: TekstFelt?,
        val borAnnenForelderISammeHusBeskrivelse: TekstFelt?,
        val boddSammenFør: BooleanFelt?,
        val flyttetFra: DatoFelt?,
        val hvorMyeSammen: TekstFelt?,
        val beskrivSamværUtenBarn: TekstFelt?
)

