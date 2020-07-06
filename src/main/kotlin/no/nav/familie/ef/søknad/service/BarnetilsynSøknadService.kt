package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.integration.SøknadClient
import no.nav.familie.ef.søknad.integration.dto.KvitteringDto
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class BarnetilsynSøknadService(val søknadClient: SøknadClient) {

    fun sendInn(innsendingMottatt: LocalDateTime): KvitteringDto {
        throw NotImplementedError("Innsending for barnetilsyn er ikke implementert")
    }
}