package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.integration.FamilieDokumentClient
import no.nav.familie.ef.søknad.integration.FamilieDokumentSbsClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException

@Service
internal class DokumentServiceImpl(private val client: FamilieDokumentClient,
                                   private val sbsClient: FamilieDokumentSbsClient) : DokumentService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun hentVedlegg(vedleggsId: String): ByteArray {
        return try {
            logger.info("Henter vedlegg $vedleggsId fra gcp")
            val hentVedlegg = client.hentVedlegg(vedleggsId)
            logger.info("Fant vedlegg=$vedleggsId i gcp")
            hentVedlegg
        } catch (e: HttpClientErrorException.BadRequest) {
            logger.warn("Finner ikke vedlegg=$vedleggsId i gcp - prøver å hente fra sbs")
            sbsClient.hentVedlegg(vedleggsId)
        }
    }

}
