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

data class Aksjeselskap (
        val navn: TekstFelt,
        val arbeidsmengde: TekstFelt
)

data class Arbeidsgiver(
    val ansettelsesforhold: TekstFelt,
    val arbeidsmengde: TekstFelt,
    val id: String,
    val navn: TekstFelt,
    val harSluttDato: BooleanFelt?,
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
        val arbeidsmengde: TekstFelt?, //TODO valider - nullable hvis heltid
        val harTattUtdanningEtterGrunnskolen: BooleanFelt,
        val heltidEllerDeltid: TekstFelt,
        val linjeKursGrad: TekstFelt,
        val målMedUtdanning: TekstFelt?, //TODO valider - nullable hvis heltid
        val offentligEllerPrivat: TekstFelt,
        val periode: Periode,
        val skoleUtdanningssted: TekstFelt,
        val tidligereUtdanning: List<TidligereUtdanning>? = null
)

data class TidligereUtdanning(
        val linjeKursGrad: TekstFelt,
        val periode: Periode
)