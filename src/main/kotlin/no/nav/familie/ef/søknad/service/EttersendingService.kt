package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.EttersendingDto
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.ÅpenEttersendingDto
import no.nav.familie.ef.søknad.integration.SøknadClient
import no.nav.familie.ef.søknad.mapper.KvitteringMapper
import no.nav.familie.ef.søknad.mapper.kontrakt.EttersendingMapper
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class EttersendingService (private val søknadClient: SøknadClient,
                           private val ettersendingMapper: EttersendingMapper,
) {

    fun sendInn(ettersending: EttersendingDto, innsendingMottatt: LocalDateTime): Kvittering {
        val ettersedingRequestData = ettersendingMapper.mapTilIntern(ettersending, innsendingMottatt)
        val kvittering = søknadClient.sendInnEttersending(ettersedingRequestData)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)
    }

    fun sendInnÅpenEttersending(åpenEttersending: ÅpenEttersendingDto, innsendingMottatt: LocalDateTime): Kvittering {
        val ettersedingRequestData = ettersendingMapper.mapTilIntern(åpenEttersending, innsendingMottatt)
        val kvittering = søknadClient.sendInnEttersending(ettersedingRequestData)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)

    }

}
