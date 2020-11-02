package no.nav.familie.ef.søknad.health

import no.nav.familie.ef.søknad.integration.FamilieDokumentSbsClient
import no.nav.familie.http.health.AbstractHealthIndicator
import org.springframework.stereotype.Component

@Component
internal class FamilieDokumentSbsHealthIndicator(client: FamilieDokumentSbsClient)
    : AbstractHealthIndicator(client, "familie.dokumentsbs")

