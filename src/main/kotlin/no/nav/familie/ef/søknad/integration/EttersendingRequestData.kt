package no.nav.familie.ef.søknad.integration

import no.nav.familie.kontrakter.ef.ettersending.EttersendingMedVedlegg

data class EttersendingRequestData<Ettersending>(
        val ettersendingMedVedlegg: EttersendingMedVedlegg,
        val vedlegg: Map<String, ByteArray>)