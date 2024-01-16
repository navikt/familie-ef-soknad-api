package no.nav.familie.ef.søknad.søknad.dto

import no.nav.familie.ef.søknad.søknad.domain.Adresseopplysninger
import no.nav.familie.ef.søknad.søknad.domain.Aktivitet
import no.nav.familie.ef.søknad.søknad.domain.BooleanFelt
import no.nav.familie.ef.søknad.søknad.domain.Bosituasjon
import no.nav.familie.ef.søknad.søknad.domain.Dokumentasjonsbehov
import no.nav.familie.ef.søknad.søknad.domain.Medlemskap
import no.nav.familie.ef.søknad.søknad.domain.Person
import no.nav.familie.ef.søknad.søknad.domain.Situasjon
import no.nav.familie.ef.søknad.søknad.domain.Sivilstatus
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
