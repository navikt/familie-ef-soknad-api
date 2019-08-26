package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.KvitteringDto
import no.nav.familie.ef.søknad.api.dto.SøknadDto

interface SøknadService {

    fun sendInn(søknadDto: SøknadDto): KvitteringDto

}