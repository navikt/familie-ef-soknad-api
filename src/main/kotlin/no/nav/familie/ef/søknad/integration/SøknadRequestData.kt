package no.nav.familie.ef.søknad.integration

import no.nav.familie.kontrakter.ef.søknad.SøknadMedVedlegg

data class SøknadRequestData(val søknadMedVedlegg: SøknadMedVedlegg, val vedlegg: Map<String, ByteArray>)
