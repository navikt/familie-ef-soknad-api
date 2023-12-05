package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class SøknadBarnetilsynDto(
    val bosituasjon: Bosituasjon,
    val medlemskap: Medlemskap,
    val person: Person,
    val sivilstatus: Sivilstatus,
    val søkerBorPåRegistrertAdresse: BooleanFelt?,
    val adresseopplysninger: Adresseopplysninger?,
    val aktivitet: Aktivitet,
    val søkerFraBestemtMåned: BooleanFelt,
    val søknadsdato: DatoFelt?,
    val dokumentasjonsbehov: List<Dokumentasjonsbehov>,
    val locale: String = "nb",
    val skalBehandlesINySaksbehandling: Boolean = false,
)

data class SøknadBarnetilsynGjenbrukDto(
    val sivilstatus: SivilstatusTilGjenbruk,
    val medlemskap: Medlemskap,
    val bosituasjon: Bosituasjon,
)
