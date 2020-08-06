package no.nav.familie.ef.søknad.api.dto.søknadsdialog

import javax.validation.Valid

class SøknadBarnetilsynDto(
        val bosituasjon: Bosituasjon,
        val medlemskap: Medlemskap,
        @field:Valid val person: Person,
        val sivilstatus: Sivilstatus,
        val søkerBorPåRegistrertAdresse: BooleanFelt?,
        val aktivitet: Aktivitet,
        val søkerFraBestemtMåned: BooleanFelt,
        val søknadsdato: DatoFelt?,
        val dokumentasjonsbehov: List<Dokumentasjonsbehov>
)