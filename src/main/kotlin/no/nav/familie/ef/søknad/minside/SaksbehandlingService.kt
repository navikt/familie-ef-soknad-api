package no.nav.familie.ef.søknad.minside

import no.nav.familie.ef.søknad.minside.dto.MineStønaderDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SaksbehandlingService(private val saksbehandlingClient: SaksbehandlingClient) {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val secureLogger = LoggerFactory.getLogger("secureLogger")

    fun hentStønadsperioderForBruker(): MineStønaderDto = saksbehandlingClient.hentStønadsperioderForBruker()
}
