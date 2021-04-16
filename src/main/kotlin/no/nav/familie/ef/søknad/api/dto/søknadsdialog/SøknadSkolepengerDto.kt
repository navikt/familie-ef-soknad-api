package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class SøknadSkolepengerDto(
        val bosituasjon: Bosituasjon,
        val medlemskap: Medlemskap,
        val person: Person,
        val sivilstatus: Sivilstatus,
        val søkerBorPåRegistrertAdresse: BooleanFelt?,
        val utdanning: UnderUtdanning,
        val dokumentasjonsbehov: List<Dokumentasjonsbehov>,
        val locale: String = "nb",
        val skalBehandlesINySaksbehandling: Boolean = true,
)
