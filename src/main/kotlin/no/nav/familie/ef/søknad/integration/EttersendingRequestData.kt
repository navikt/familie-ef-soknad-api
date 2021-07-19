package no.nav.familie.ef.søknad.integration

import no.nav.familie.kontrakter.ef.ettersending.EttersendingMedVedlegg

data class EttersendingRequestData<T>(
    val ettersendingMedVedlegg: EttersendingMedVedlegg<T>,
    val vedlegg: Map<String, ByteArray>
)