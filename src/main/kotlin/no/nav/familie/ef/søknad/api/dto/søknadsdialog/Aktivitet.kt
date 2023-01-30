package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Aktivitet(
    val arbeidsforhold: List<Arbeidsgiver>?,
    val arbeidssøker: Arbeidssøker? = null,
    @Deprecated("Bruk firmaer istedenfor") val firma: Firma? = null,
    val firmaer: List<Firma>?,
    val hvaErDinArbeidssituasjon: ListFelt<String>,
    val underUtdanning: UnderUtdanning? = null,
    val etablererEgenVirksomhet: TekstFelt? = null,
    val egetAS: List<Aksjeselskap>?,
    val datoOppstartJobb: DatoFelt? = null,
    val erIArbeid: TekstFelt?,
)

data class Aksjeselskap(
    val navn: TekstFelt,
    val arbeidsmengde: TekstFelt?,
)

data class Arbeidsgiver(
    val ansettelsesforhold: TekstFelt,
    val arbeidsmengde: TekstFelt?,
    val id: String,
    val navn: TekstFelt,
    val harSluttDato: BooleanFelt?,
    val sluttdato: DatoFelt?,
)

data class Arbeidssøker(
    val hvorØnskerSøkerArbeid: TekstFelt,
    val kanBegynneInnenEnUke: BooleanFelt,
    val kanSkaffeBarnepassInnenEnUke: BooleanFelt?,
    val registrertSomArbeidssøkerNav: BooleanFelt,
    val villigTilÅTaImotTilbudOmArbeid: BooleanFelt,
    val ønskerSøker50ProsentStilling: BooleanFelt,
    val locale: String = "nb",
)

data class Firma(
    val arbeidsmengde: TekstFelt?,
    val arbeidsuke: TekstFelt,
    val etableringsdato: DatoFelt,
    val navn: TekstFelt,
    val organisasjonsnummer: TekstFelt,
)

data class UnderUtdanning(
    val arbeidsmengde: TekstFelt?, // TODO valider - nullable hvis heltid og ikke med i Barnetilsyn
    val harTattUtdanningEtterGrunnskolen: BooleanFelt,
    val heltidEllerDeltid: TekstFelt,
    val linjeKursGrad: TekstFelt,
    val målMedUtdanning: TekstFelt?, // TODO valider - nullable hvis heltid
    val offentligEllerPrivat: TekstFelt,
    val periode: PeriodeFelt,
    val skoleUtdanningssted: TekstFelt,
    val tidligereUtdanning: List<TidligereUtdanning>? = null,
    val semesteravgift: TekstFelt? = null, // Skolepenger
    val studieavgift: TekstFelt? = null, // Skolepenger
    val eksamensgebyr: TekstFelt? = null, // Skolepenger
)

data class TidligereUtdanning(
    val linjeKursGrad: TekstFelt,
    val periode: PeriodeFelt,
)
