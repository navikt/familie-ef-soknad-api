package no.nav.familie.ef.søknad.integration

import no.nav.familie.kontrakter.ef.søknad.SøknadMedVedlegg

data class SøknadRequestData<T>(val søknadMedVedlegg: SøknadMedVedlegg<T>, val vedlegg: Map<String, ByteArray>)
