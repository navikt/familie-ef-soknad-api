package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.integration.FamilieDokumentClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
internal class DokumentServiceImpl(private val client: FamilieDokumentClient) : DokumentService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun hentVedlegg(vedleggsId: String): ByteArray {
        logger.info("Henter vedlegg $vedleggsId fra gcp")
        return client.hentVedlegg(vedleggsId)
    }
}


