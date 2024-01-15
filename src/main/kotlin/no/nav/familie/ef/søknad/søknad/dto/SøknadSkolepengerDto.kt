package no.nav.familie.ef.søknad.søknad.dto

import no.nav.familie.ef.søknad.søknad.domain.Adresseopplysninger
import no.nav.familie.ef.søknad.søknad.domain.BooleanFelt
import no.nav.familie.ef.søknad.søknad.domain.Bosituasjon
import no.nav.familie.ef.søknad.søknad.domain.Dokumentasjonsbehov
import no.nav.familie.ef.søknad.søknad.domain.Medlemskap
import no.nav.familie.ef.søknad.søknad.domain.Person
import no.nav.familie.ef.søknad.søknad.domain.Sivilstatus
import no.nav.familie.ef.søknad.søknad.domain.UnderUtdanning
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
