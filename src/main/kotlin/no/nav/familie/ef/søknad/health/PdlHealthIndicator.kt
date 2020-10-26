package no.nav.familie.ef.søknad.health

import no.nav.familie.ef.søknad.integration.PdlClient
import no.nav.familie.http.health.AbstractHealthIndicator
import org.springframework.stereotype.Component

@Component
internal class PdlHealthIndicator(pdlClient: PdlClient)
    : AbstractHealthIndicator(pdlClient, "pdl.personinfo")

