package no.nav.familie.ef.søknad.infrastruktur.health

import no.nav.familie.ef.søknad.søknad.MottakClient
import no.nav.familie.restklient.health.AbstractHealthIndicator
import org.springframework.stereotype.Component

@Component
internal class SøknadHealthIndicator(
    søknadClient: MottakClient,
) : AbstractHealthIndicator(søknadClient, "familie.ef.mottak")
