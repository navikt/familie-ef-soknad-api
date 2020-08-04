package no.nav.familie.ef.søknad.integration

import no.nav.familie.ef.søknad.mapper.kontrakt.SøknadBarnetilsyn

// TODO - bytt ut med BarnetilsynSøknad fra kontrakter
data class BarnetilsynRequestData(val barnetilsyn: SøknadBarnetilsyn, val vedlegg: Map<String, ByteArray>)
