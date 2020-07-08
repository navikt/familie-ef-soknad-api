package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.integration.FamilieDokumentClient
import org.springframework.stereotype.Service

@Service
internal class DokumentServiceImpl(private val client: FamilieDokumentClient) : DokumentService {

    override fun hentVedlegg(vedleggsId: String): ByteArray {
        return client.hentVedlegg(vedleggsId)
    }

//    override fun hentDokumenter(dokumentasjonsbehov: List<Dokumentasjonsbehov>): Map<String, ByteArray> {
//        return dokumentasjonsbehov.flatMap { dok ->
//            dok.opplastedeVedlegg.map {
//                it.dokumentId to hentVedlegg(it.dokumentId)
//            }
//        }.toMap()
//    }
}
