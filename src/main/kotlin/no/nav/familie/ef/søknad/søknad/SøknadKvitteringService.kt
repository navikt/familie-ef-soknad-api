package no.nav.familie.ef.søknad.søknad

import org.springframework.stereotype.Service

@Service
class SøknadKvitteringService(
    private val søknadKvitteringClient: SøknadKvitteringClient,
) {
    fun hentSøknadPdf(søknadId: String): ByteArray = søknadKvitteringClient.hentSøknadKvittering(søknadId)
}
