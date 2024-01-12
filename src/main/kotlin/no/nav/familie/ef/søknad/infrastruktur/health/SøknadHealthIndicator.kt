package no.nav.familie.ef.søknad.infrastruktur.health

import no.nav.familie.ef.søknad.søknad.SøknadClient
import no.nav.familie.http.health.AbstractHealthIndicator
import org.springframework.stereotype.Component

@Component
internal class SøknadHealthIndicator(søknadClient: SøknadClient) :
    AbstractHealthIndicator(søknadClient, "familie.ef.mottak")
