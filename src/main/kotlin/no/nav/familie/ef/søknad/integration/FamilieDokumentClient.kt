package no.nav.familie.ef.søknad.integration

import no.nav.familie.ef.søknad.config.FamilieDokumentConfig
import no.nav.familie.ef.søknad.util.URIUtil
import no.nav.familie.kontrakter.felles.Ressurs
import org.springframework.stereotype.Component
import org.springframework.web.client.RestOperations

@Component
internal class FamilieDokumentClient(val config: FamilieDokumentConfig,
                                     operations: RestOperations) : PingableRestClient(operations, config.pingUri) {
    fun hentVedlegg(vedleggsId: String): ByteArray {
        val ressurs = getForEntity(URIUtil.uri(config.hentVedleggUri, vedleggsId)) as Ressurs<String>
        return ressurs.data?.toByteArray(Charsets.UTF_8) ?: error("Ingen data på ressurs ved henting av vedlegg")
    }
}
