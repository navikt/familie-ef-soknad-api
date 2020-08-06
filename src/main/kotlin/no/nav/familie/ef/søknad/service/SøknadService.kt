package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadOvergangsstønadDto
import java.time.LocalDateTime

interface SøknadService {

    fun sendInn(søknad: SøknadOvergangsstønadDto, innsendingMottatt: LocalDateTime): Kvittering

}