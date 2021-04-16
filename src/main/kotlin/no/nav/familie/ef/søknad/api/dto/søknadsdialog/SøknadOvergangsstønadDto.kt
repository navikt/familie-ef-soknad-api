package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class SøknadOvergangsstønadDto(val bosituasjon: Bosituasjon,
                                    val medlemskap: Medlemskap,
                                    val person: Person,
                                    val sivilstatus: Sivilstatus,
                                    val søkerBorPåRegistrertAdresse: BooleanFelt? = null,
                                    val dokumentasjonsbehov: List<Dokumentasjonsbehov>,
                                    val aktivitet: Aktivitet,
                                    val merOmDinSituasjon: Situasjon,
                                    val locale: String = "nb",
                                    val skalBehandlesINySaksbehandling: Boolean = false
)
