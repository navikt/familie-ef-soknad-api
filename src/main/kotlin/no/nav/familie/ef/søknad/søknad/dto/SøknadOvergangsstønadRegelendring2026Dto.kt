package no.nav.familie.ef.søknad.søknad.dto

import no.nav.familie.ef.søknad.søknad.domain.Adresseopplysninger
import no.nav.familie.ef.søknad.søknad.domain.BooleanFelt
import no.nav.familie.ef.søknad.søknad.domain.Bosituasjon
import no.nav.familie.ef.søknad.søknad.domain.DatoFelt
import no.nav.familie.ef.søknad.søknad.domain.Dokumentasjonsbehov
import no.nav.familie.ef.søknad.søknad.domain.Firma
import no.nav.familie.ef.søknad.søknad.domain.ListFelt
import no.nav.familie.ef.søknad.søknad.domain.Medlemskap
import no.nav.familie.ef.søknad.søknad.domain.Person
import no.nav.familie.ef.søknad.søknad.domain.Sivilstatus
import no.nav.familie.ef.søknad.søknad.domain.TekstFelt
import java.time.LocalDate

data class SøknadOvergangsstønadRegelendring2026Dto(
    val erRegelendring2026: Boolean = true,
    val person: Person,
    val søkerBorPåRegistrertAdresse: BooleanFelt? = null,
    val adresseopplysninger: Adresseopplysninger? = null,
    val sivilstatus: Sivilstatus,
    val medlemskap: Medlemskap,
    val bosituasjon: Bosituasjon,
    val hvaSituasjon: ListFelt<String>,
    val inntekter: ListFelt<String>,
    val firmaer: List<Firma>? = null,
    val sagtOppEllerRedusertStilling: TekstFelt? = null,
    val begrunnelseSagtOppEllerRedusertStilling: TekstFelt? = null,
    val datoSagtOppEllerRedusertStilling: DatoFelt? = null,
    val søkerFraBestemtMåned: BooleanFelt? = null,
    val søknadsdato: DatoFelt? = null,
    val dokumentasjonsbehov: List<Dokumentasjonsbehov>,
    val locale: String = "nb",
    val datoPåbegyntSøknad: LocalDate? = null,
)
