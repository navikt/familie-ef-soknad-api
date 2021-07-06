package no.nav.familie.ef.søknad.api.dto.søknadsdialog

data class EttersendingDto(val person: Person,
                            val dokumentasjonsbehov: List<Dokumentasjonsbehov>,
)
