package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.integration.SøknadClient
import no.nav.familie.ef.søknad.mapper.KvitteringMapper
import no.nav.familie.ef.søknad.mapper.kontrakt.EttersendingMapper
import no.nav.familie.kontrakter.ef.ettersending.EttersendingDto
import no.nav.familie.kontrakter.ef.ettersending.EttersendingResponseData
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class EttersendingService(private val søknadClient: SøknadClient,
                          private val ettersendingMapper: EttersendingMapper) {

    fun sendInn(ettersending: EttersendingDto, innsendingMottatt: LocalDateTime): Kvittering {
        val ettersedingRequestData = ettersendingMapper.mapTilIntern(ettersending, innsendingMottatt)
        val kvittering = søknadClient.sendInnEttersending(ettersedingRequestData)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)
    }


    fun hentEttersendingForPerson(personIdent: String): List<EttersendingResponseData> {
        return søknadClient.hentEttersendingForPerson(personIdent);
    }

}
