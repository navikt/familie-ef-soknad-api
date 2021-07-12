package no.nav.familie.ef.søknad.api.dto.søknadsdialog

import no.nav.familie.kontrakter.ef.søknad.Dokumentasjonsbehov

data class EttersendingDto(val person: Person,
                            val dokumentasjonsbehov: List<Dokumentasjonsbehov>,
)
