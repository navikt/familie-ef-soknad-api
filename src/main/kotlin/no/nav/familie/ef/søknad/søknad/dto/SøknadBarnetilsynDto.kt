package no.nav.familie.ef.søknad.søknad.dto

import no.nav.familie.ef.søknad.søknad.domain.Adresseopplysninger
import no.nav.familie.ef.søknad.søknad.domain.Aktivitet
import no.nav.familie.ef.søknad.søknad.domain.BooleanFelt
import no.nav.familie.ef.søknad.søknad.domain.Bosituasjon
import no.nav.familie.ef.søknad.søknad.domain.DatoFelt
import no.nav.familie.ef.søknad.søknad.domain.Dokumentasjonsbehov
import no.nav.familie.ef.søknad.søknad.domain.Medlemskap
import no.nav.familie.ef.søknad.søknad.domain.Person
import no.nav.familie.ef.søknad.søknad.domain.PersonTilGjenbruk
import no.nav.familie.ef.søknad.søknad.domain.Sivilstatus
import no.nav.familie.ef.søknad.søknad.domain.SivilstatusTilGjenbruk
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
    val aktivitet: Aktivitet,
    val locale: String,
)
