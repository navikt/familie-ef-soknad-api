package no.nav.familie.ef.søknad.api.dto.søknadsdialog

import java.time.LocalDate

data class SøknadSkolepengerDto(
    val bosituasjon: Bosituasjon,
    val medlemskap: Medlemskap,
    val person: Person,
    val sivilstatus: Sivilstatus,
    val søkerBorPåRegistrertAdresse: BooleanFelt?,
    val adresseopplysninger: Adresseopplysninger?,
    val utdanning: UnderUtdanning,
    val dokumentasjonsbehov: List<Dokumentasjonsbehov>,
    val locale: String = "nb",
    val skalBehandlesINySaksbehandling: Boolean = false,
    val datoPåbegyntSøknad: LocalDate? = null,
)
