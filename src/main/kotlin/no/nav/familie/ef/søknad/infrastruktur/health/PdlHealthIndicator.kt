package no.nav.familie.ef.søknad.infrastruktur.health

import no.nav.familie.ef.søknad.person.PdlClient
import no.nav.familie.restklient.health.AbstractHealthIndicator
import org.springframework.stereotype.Component

@Component
internal class PdlHealthIndicator(
    pdlClient: PdlClient,
) : AbstractHealthIndicator(pdlClient, "pdl.personinfo")
