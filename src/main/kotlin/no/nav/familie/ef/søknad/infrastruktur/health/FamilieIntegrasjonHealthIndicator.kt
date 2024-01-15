package no.nav.familie.ef.søknad.infrastruktur.health

import no.nav.familie.ef.søknad.infrastruktur.kodeverk.FamilieIntegrasjonerClient
import no.nav.familie.http.health.AbstractHealthIndicator
import org.springframework.stereotype.Component

@Component
internal class FamilieIntegrasjonHealthIndicator(familieIntegrasjonerClient: FamilieIntegrasjonerClient) :
    AbstractHealthIndicator(familieIntegrasjonerClient, "familie.integrasjoner")
