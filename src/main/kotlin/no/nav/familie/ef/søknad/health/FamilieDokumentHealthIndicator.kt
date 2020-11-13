package no.nav.familie.ef.søknad.health

import no.nav.familie.ef.søknad.integration.FamilieDokumentClient
import no.nav.familie.http.health.AbstractHealthIndicator
import org.springframework.stereotype.Component

@Component
internal class FamilieDokumentHealthIndicator(client: FamilieDokumentClient)
    : AbstractHealthIndicator(client, "familie.dokument")

