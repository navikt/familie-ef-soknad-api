package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto

interface SøknadService {

    fun sendInn(søknad: SøknadDto): Kvittering

}