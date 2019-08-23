package no.nav.familie.ef.soknad.service

import no.nav.familie.ef.soknad.api.dto.KvitteringDto
import no.nav.familie.ef.soknad.api.dto.SøknadDto

interface SøknadService {


    fun sendInn(søknadDto: SøknadDto): KvitteringDto

}