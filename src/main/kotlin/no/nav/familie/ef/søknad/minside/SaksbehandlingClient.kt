package no.nav.familie.ef.søknad.minside

import no.nav.familie.ef.søknad.infrastruktur.config.SaksbehandlingConfig
import no.nav.familie.ef.søknad.minside.dto.StønadsperioderDto
import no.nav.familie.http.client.AbstractPingableRestClient
import no.nav.familie.kontrakter.felles.Ressurs
import no.nav.familie.kontrakter.felles.getDataOrThrow
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.client.RestOperations
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Service
class SaksbehandlingClient(
    private val config: SaksbehandlingConfig,
    @Qualifier("tokenExchange") operations: RestOperations,
) :
    AbstractPingableRestClient(operations, "saksbehandling") {

    override val pingUri: URI = config.pingUri

    fun hentStønadsperioderForBruker() = getForEntity<Ressurs<StønadsperioderDto>>(
        UriComponentsBuilder.fromUriString("${config.hentStønadsperioderUri}").build().toUri(),
    ).getDataOrThrow()
}
