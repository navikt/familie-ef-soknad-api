package no.nav.familie.ef.søknad.kodeverk

import no.nav.familie.ef.søknad.infrastruktur.config.FamilieIntegrasjonerConfig
import no.nav.familie.http.client.AbstractPingableRestClient
import no.nav.familie.kontrakter.felles.Ressurs
import no.nav.familie.kontrakter.felles.kodeverk.KodeverkDto
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations
import java.net.URI

@Component
class FamilieIntegrasjonerClient(
    private val config: FamilieIntegrasjonerConfig,
    @Qualifier("utenAuth") restTemplate: RestOperations,
) : AbstractPingableRestClient(restTemplate, "familie.integrasjoner") {
    override val pingUri: URI = config.pingUri

    fun hentKodeverkLandkoder(): KodeverkDto = getForEntity<Ressurs<KodeverkDto>>(config.kodeverkLandkoderUri).data!!

    fun hentKodeverkPoststed(): KodeverkDto = getForEntity<Ressurs<KodeverkDto>>(config.kodeverkPoststedUri).data!!
}
