package no.nav.familie.ef.søknad.api.dto.søknadsdialog

import javax.validation.Valid

data class SøknadOvergangsstønadDto(val bosituasjon: Bosituasjon,
                                    val medlemskap: Medlemskap,
                                    @field:Valid val person: Person,
                                    val sivilstatus: Sivilstatus,
                                    val søkerBorPåRegistrertAdresse: BooleanFelt? = null,
                                    val dokumentasjonsbehov: List<Dokumentasjonsbehov>,
                                    val aktivitet: Aktivitet,
                                    val merOmDinSituasjon: Situasjon)
