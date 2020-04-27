package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Arbeidssøker


interface SkjemaService {

    fun sendInn(søknad: Arbeidssøker, fnr : String): Kvittering

}