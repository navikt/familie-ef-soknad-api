package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Aktivitet(
        val arbeidsforhold: List<Arbeidsgiver>?,
        val arbeidssøker: Arbeidssøker?=null,
        val firma: Firma? = null,
        val hvaErDinArbeidssituasjon: ListFelt<String>,
        val underUtdanning: UnderUtdanning? = null,
        val etablererEgenVirksomhet: TekstFelt? = null,
        val egetAS: List<Aksjeselskap>?
)

data class Aksjeselskap ( // TODO - ikke mappet
        val navn: TekstFelt?= null,
        val arbeidsmengde: TekstFelt?= null
)

data class Arbeidsgiver(
    val ansettelsesforhold: TekstFelt,
    val arbeidsmengde: TekstFelt,
    val id: String,
    val navn: TekstFelt,
    val harSluttDato: BooleanFelt, // TODO Booleanfelt skal ikke være null - fix i UI, eller i kontrakt?
    val sluttdato: DatoFelt?
)


data class Arbeidssøker(
        val hvorØnskerSøkerArbeid: TekstFelt,
        val kanBegynneInnenEnUke: BooleanFelt,
        val kanSkaffeBarnepassInnenEnUke: BooleanFelt,
        val registrertSomArbeidssøkerNav: BooleanFelt,
        val villigTilÅTaImotTilbudOmArbeid: BooleanFelt,
        val ønskerSøker50ProsentStilling: BooleanFelt
)

data class Firma(
        val arbeidsmengde: TekstFelt,
        val arbeidsuke: TekstFelt,
        val etableringsdato: DatoFelt,
        val navn: TekstFelt,
        val organisasjonsnummer: TekstFelt
)

data class UnderUtdanning(
        val arbeidsmengde: TekstFelt,
        val harTattUtdanningEtterGrunnskolen: BooleanFelt,
        val heltidEllerDeltid: TekstFelt,
        val linjeKursGrad: TekstFelt,
        val målMedUtdanning: TekstFelt,
        val offentligEllerPrivat: TekstFelt,
        val periode: Periode,
        val react_key: String,
        val skoleUtdanningssted: TekstFelt,
        val tidligereUtdanning: List<TidligereUtdanning>? = null
)

data class TidligereUtdanning(
        val linjeKursGrad: TekstFelt,
        val periode: Periode,
        val react_key: String
)