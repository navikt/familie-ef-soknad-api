package no.nav.familie.ef.søknad.health

import no.nav.familie.ef.søknad.integration.SøknadClient
import no.nav.familie.http.health.AbstractHealthIndicator
import org.springframework.stereotype.Component

@Component
internal class SøknadHealthIndicator(søknadClient: SøknadClient) :
    AbstractHealthIndicator(søknadClient, "familie.ef.mottak")
