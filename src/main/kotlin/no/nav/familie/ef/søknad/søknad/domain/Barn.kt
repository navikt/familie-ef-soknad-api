package no.nav.familie.ef.søknad.søknad.domain

data class Barn(
    val alder: TekstFelt?, // Bare for visning (kan fjernes?)
    val ident: TekstFelt? = null,
    val fødselsdato: DatoFelt?,
    val harSammeAdresse: BooleanFelt,
    val ikkeRegistrertPåSøkersAdresseBeskrivelse: TekstFelt?,
    val id: String? = null,
    val lagtTil: Boolean? = false,
    val navn: TekstFelt?, // optional på nye barn
    val født: BooleanFelt,
    val forelder: AnnenForelder? = null, // Dersom annen forelder er død så skal denne være null
    val skalHaBarnepass: BooleanFelt?,
    val særligeTilsynsbehov: TekstFelt?,
    val barnepass: Barnepass?,
)

data class AnnenForelder(
    val ikkeOppgittAnnenForelderBegrunnelse: TekstFelt?, // Dekker både Donorbarn og Annet-begrunnelse
    val kanIkkeOppgiAnnenForelderFar: BooleanFelt?,
    val hvorforIkkeOppgi: TekstFelt?,
    val navn: TekstFelt?,
    val fødselsdato: DatoFelt?,
    val ident: TekstFelt? = null,
    val borINorge: BooleanFelt?,
    val land: TekstFelt?,
    val harAnnenForelderSamværMedBarn: TekstFelt?,
    val harDereSkriftligSamværsavtale: TekstFelt?,
    val hvordanPraktiseresSamværet: TekstFelt?,
    val borAnnenForelderISammeHus: TekstFelt?,
    val borAnnenForelderISammeHusBeskrivelse: TekstFelt?,
    val boddSammenFør: BooleanFelt?,
    val flyttetFra: DatoFelt?,
    val hvorMyeSammen: TekstFelt?,
    val beskrivSamværUtenBarn: TekstFelt?,
    val skalBarnetBoHosSøker: TekstFelt?,
)
