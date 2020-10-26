package no.nav.familie.ef.søknad.health

import no.nav.familie.ef.søknad.integration.PdlStsClient
import no.nav.familie.http.health.AbstractHealthIndicator
import org.springframework.stereotype.Component

@Component
internal class PdlHealthIndicator(pdlStsClient: PdlStsClient)
    : AbstractHealthIndicator(pdlStsClient, "pdl.personinfo")

