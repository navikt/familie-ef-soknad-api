package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
import no.nav.familie.ef.søknad.integration.SøknadClient
import no.nav.familie.ef.søknad.mapper.KvitteringMapper
import no.nav.familie.ef.søknad.mapper.kontrakt.EttersendingMapper
import no.nav.familie.kontrakter.ef.ettersending.EttersendingDto
import no.nav.familie.kontrakter.ef.ettersending.EttersendingResponseData
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class EttersendingService (private val søknadClient: SøknadClient,
                           private val ettersendingMapper: EttersendingMapper,
                           private val featureToggleService: FeatureToggleService) {

    fun sendInn(ettersending: EttersendingDto, innsendingMottatt: LocalDateTime): Kvittering {
        val ettersedingRequestData = ettersendingMapper.mapTilIntern(ettersending, innsendingMottatt, skalHenteVedlegg())
        val kvittering = sendInn(ettersedingRequestData, søknadClient::sendInnEttersendingOld, søknadClient::sendInnEttersending)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)
    }


    fun hentEttersendingForPerson(personIdent: String):List<EttersendingResponseData>{
        return søknadClient.hentEttersendingForPerson(personIdent);
    }

    private fun <T, R> sendInn(request: T, v1: (T) -> R, v2: (T) -> R): R {
        return if (skalHenteVedlegg()) {
            v1.invoke(request)
        } else {
            v2.invoke(request)
        }
    }

    private fun skalHenteVedlegg() =
            !featureToggleService.isEnabled("familie.ef.soknad.api.dokumentFraMottak")

}
