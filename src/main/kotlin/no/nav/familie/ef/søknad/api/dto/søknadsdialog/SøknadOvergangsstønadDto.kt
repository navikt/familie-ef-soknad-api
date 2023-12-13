package no.nav.familie.ef.søknad.api.dto.søknadsdialog

import java.time.LocalDate

data class SøknadOvergangsstønadDto(
    val bosituasjon: Bosituasjon,
    val medlemskap: Medlemskap,
    val person: Person,
    val sivilstatus: Sivilstatus,
    val søkerBorPåRegistrertAdresse: BooleanFelt? = null,
    val adresseopplysninger: Adresseopplysninger?,
    val dokumentasjonsbehov: List<Dokumentasjonsbehov>,
    val aktivitet: Aktivitet,
    val merOmDinSituasjon: Situasjon,
    val locale: String = "nb",
    val skalBehandlesINySaksbehandling: Boolean = false,
    val datoPåbegyntSøknad: LocalDate? = null,
)
