package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class Aktivitet(
        val arbeidsforhold: List<Arbeidsforhold>?,
        val arbeidssøker: Arbeidssøker?=null,
        val firma: Firma? = null,
        val hvaErDinArbeidssituasjon: HvaErDinArbeidssituasjon,
        val underUtdanning: UnderUtdanning? = null,
        val etablererEgenVirksomhet: TekstFelt? = null

)



data class Arbeidsforhold(
        val arbeidsmengde: TekstFelt,
        val fastStilling: TekstFelt,
        val harSluttDato: BooleanFelt,
        val sluttdato: DatoFelt,
        val navn: TekstFelt,
        val firma: Firma? = null,
        val react_key: String?
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

data class HvaErDinArbeidssituasjon(
        val label: String,
        val verdi: List<String>
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