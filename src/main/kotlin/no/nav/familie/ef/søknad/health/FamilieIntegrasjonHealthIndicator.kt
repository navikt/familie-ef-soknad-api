package no.nav.familie.ef.søknad.health

import no.nav.familie.ef.søknad.integration.FamilieIntegrasjonerClient
import no.nav.familie.http.health.AbstractHealthIndicator
import org.springframework.stereotype.Component

@Component
internal class FamilieIntegrasjonHealthIndicator(familieIntegrasjonerClient: FamilieIntegrasjonerClient) :
    AbstractHealthIndicator(familieIntegrasjonerClient, "familie.integrasjoner")
