package no.nav.familie.ef.søknad.api.dto.ettersending

import no.nav.familie.kontrakter.ef.ettersending.EttersendingDto
import java.time.LocalDateTime

data class EttersendingResponseData (
    val ettersending: EttersendingDto,
    val mottattTidspunkt: LocalDateTime
)