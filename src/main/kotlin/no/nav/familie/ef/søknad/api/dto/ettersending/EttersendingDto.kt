package no.nav.familie.ef.søknad.api.dto.ettersending

data class EttersendingDto(val fnr: String,
                           val ettersendingForSøknad: EttersendingForSøknad,
                           val ettersendingUtenSøknad: EttersendingUtenSøknad
)
