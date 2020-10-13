package no.nav.familie.ef.søknad.integration

import no.nav.familie.ef.søknad.config.FamilieDokumentConfig
import no.nav.familie.http.client.AbstractPingableRestClient
import no.nav.familie.kontrakter.felles.Ressurs
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Component
internal class FamilieDokumentClient(private val config: FamilieDokumentConfig,
                                     restTemplate: RestOperations) : AbstractPingableRestClient(restTemplate,
                                                                                                "familie.dokument") {

    override val pingUri: URI = config.pingUri

    internal fun vedleggUri(vedleggsId: String) =
            UriComponentsBuilder.fromUri(config.hentVedleggUri).path(vedleggsId).build().toUri()

    fun hentVedlegg(vedleggsId: String): ByteArray {
        val ressurs: Ressurs<ByteArray> = getForEntity(vedleggUri(vedleggsId))
        return ressurs.data ?: error("Ingen data på ressurs ved henting av vedlegg")
    }

}
