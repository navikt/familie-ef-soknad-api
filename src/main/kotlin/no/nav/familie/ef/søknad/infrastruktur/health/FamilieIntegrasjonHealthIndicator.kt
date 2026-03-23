package no.nav.familie.ef.søknad.infrastruktur.health

import no.nav.familie.ef.søknad.kodeverk.FamilieIntegrasjonerClient
import no.nav.familie.restklient.health.AbstractHealthIndicator
import org.springframework.stereotype.Component

@Component
internal class FamilieIntegrasjonHealthIndicator(
    familieIntegrasjonerClient: FamilieIntegrasjonerClient,
) : AbstractHealthIndicator(familieIntegrasjonerClient, "familie.integrasjoner")
