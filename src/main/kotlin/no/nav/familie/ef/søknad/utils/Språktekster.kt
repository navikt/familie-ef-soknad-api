package no.nav.familie.ef.søknad.utils

fun Språktekster.hentTekst(): String =
    when (språk()) {
        Språk.EN -> this.engelsk
        Språk.NB -> this.norsk
    }

enum class Språktekster(
    val norsk: String,
    val engelsk: String,
) {
    Fødselsnummer("Fødselsnummer", "National identity number or D number"),
    OmArbeidsforholdet("Om arbeidsforholdet ditt", "About your employment"),
    OmFirmaDuDriver("Om firmaet du driver", "About the company you run"),
    OmAksjeselskapetDitt("Om aksjeselskapet ditt", "About your limited liability company"),
    NårDuErArbeidssøker("Når du er arbeidssøker", "If you are a job seeker"),
    OmVirksomhetenDuEtablerer("Om virksomheten du etablerer", "About the business you are setting up"),
    OmBarnepassordning("Om barnepassordningen", "About the child minding arrangements"),
    BarnaDine("Barna dine", "Your children"),
    DenAndreForelderenSamvær("Den andre forelderen og samvær", "The other parent and access"),
    Samvær("Samvær", "Access/Contact"),
    Adresseopplysninger("Opplysninger om adresse", "Information about address"),
    Bosituasjon("Bosituasjonen din", "Your living situation"),
    Innsendingsdetaljer("Innsendingsdetaljer", "Submission details"),
    DatoMottatt("Dato mottatt", "Date received"),
    Utenlandsopphold("Utenlandsopphold", "Time abroad"),
    Søker("Søker", "Applicant"),
    Navn("Navn", "Name"),
    Statsborgerskap("Statsborgerskap", "Nationality"),
    Sivilstatus("Sivilstatus", "Marital status"),
    Telefonnummer("Telefonnummer", "Telephone number"),
    Adresse("Adresse", "Address"),
    OmSamboer("Om samboeren din", "About your cohabitant"),
    Persondata("Persondata", "Person"),
    AnnenForelderNavn("Annen forelder navn", "The other parent"),
    IkkeOppgitt("Ikke oppgitt", "Not registered"),
    MerOmSituasjonenDin("Mer om situasjonen din", "Your situation in more detail"),
    ÅrsakTilAleneMedBarn("Årsak til alene med barn", "Reason for being sole caregiver"),
    OmDenTidligereSamboeren("Om den tidligere samboeren din", "About your former cohabitant"),
    Fremtidsplaner("Fremtidsplaner", "Future plans"),
    NårSøkerDuFra("Når søker du stønad fra?", "When are you applying for benefit from?"),
    FraMåned("Fra måned", "From month"),
    FraÅr("Fra år", "From year"),
    UtdanningenDuSkalTa("Utdanningen du skal ta", "The education you are taking or are going to take"),
    Utdanning("Utdanning", "Education"),
    NårSkalDuVæreElevStudent("Når skal du være elev/student?", "When are you going to start school / studying?"),
    NårVarDuElevStudent("Når var du elev/student?", "When did you go to school / study?"),
    TidligereUtdanning("Tidligere Utdanning", "Previous education"),
    EnsligMorEllerFarSomErArbeidssøker(
        "Enslig mor og far som er arbeidssøker",
        "Single mother or father who is a job seeker",
    ),
    OppholdINorge("Opphold i Norge", "Presence in Norway"),
    Barnepassordninger("Barnepassordninger", "About the child minding arrangements"),
    SendtInnTidligere(
        "Jeg har sendt inn denne dokumentasjonen til Nav tidligere",
        "I have already submitted this documentation to Nav in the past",
    ),
    ArbeidUtanningOgAndreAktiviteter(
        "Arbeid, utdanning og andre aktiviteter",
        "Work, education and other activities",
    ),
    Alder("Alder", "Age"),
}
