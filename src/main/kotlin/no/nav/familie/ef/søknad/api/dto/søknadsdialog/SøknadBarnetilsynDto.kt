package no.nav.familie.ef.søknad.api.dto.søknadsdialog

import java.time.LocalDate

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
    val datoPåbegyntSøknad: LocalDate? = null,
)

data class SøknadBarnetilsynGjenbrukDto(
    val sivilstatus: SivilstatusTilGjenbruk,
    val medlemskap: Medlemskap,
    val bosituasjon: Bosituasjon,
    val person: PersonTilGjenbruk,
)
