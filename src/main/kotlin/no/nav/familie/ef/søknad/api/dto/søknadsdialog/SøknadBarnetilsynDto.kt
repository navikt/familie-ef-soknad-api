package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class SøknadBarnetilsynDto(
        val bosituasjon: Bosituasjon,
        val medlemskap: Medlemskap,
        val person: Person,
        val sivilstatus: Sivilstatus,
        val søkerBorPåRegistrertAdresse: BooleanFelt?,
        val aktivitet: Aktivitet,
        val søkerFraBestemtMåned: BooleanFelt,
        val søknadsdato: DatoFelt?,
        val dokumentasjonsbehov: List<Dokumentasjonsbehov>
)