package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.integration.FamilieDokumentClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException

@Service
internal class DokumentServiceImpl(private val client: FamilieDokumentClient) : DokumentService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun hentVedlegg(vedleggsId: String): ByteArray {
        try {
            logger.info("Henter vedlegg $vedleggsId fra gcp")
            val hentVedlegg = client.hentVedlegg(vedleggsId)
            logger.info("Fant vedlegg=$vedleggsId i gcp")
            return hentVedlegg
        } catch (e: HttpClientErrorException.NotFound) {
            logger.warn("Finner ikke vedlegg=$vedleggsId i gcp")
            throw (e)
        }
    }
}


