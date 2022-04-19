package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.integration.SøknadClient
import no.nav.familie.ef.søknad.mapper.KvitteringMapper
import no.nav.familie.ef.søknad.mapper.kontrakt.EttersendingMapper
import no.nav.familie.kontrakter.ef.ettersending.EttersendelseDto
import no.nav.familie.kontrakter.felles.ef.StønadType
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class EttersendingService(private val søknadClient: SøknadClient) {

    fun sendInn(ettersending: EttersendelseDto, innsendingMottatt: LocalDateTime): Kvittering {
        val ettersedingRequestData: Map<StønadType, EttersendelseDto> = EttersendingMapper.groupByStønad(ettersending, innsendingMottatt)
        val kvittering = søknadClient.sendInnEttersending(ettersedingRequestData)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)
    }


    fun hentEttersendingForPerson(personIdent: String): List<EttersendelseDto> {
        return søknadClient.hentEttersendingForPerson(personIdent)
    }

}
