package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.api.dto.Søknad

interface SøknadService {

    fun sendInn(søknad: Søknad): Kvittering

}