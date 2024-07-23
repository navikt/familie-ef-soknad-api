package no.nav.familie.ef.søknad.ettersending

import no.nav.familie.ef.søknad.ettersending.mapper.EttersendingMapper
import no.nav.familie.ef.søknad.søknad.SøknadClient
import no.nav.familie.ef.søknad.søknad.domain.Kvittering
import no.nav.familie.ef.søknad.søknad.mapper.KvitteringMapper
import no.nav.familie.kontrakter.ef.ettersending.EttersendelseDto
import no.nav.familie.kontrakter.felles.ef.StønadType
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class EttersendingService(
    private val søknadClient: SøknadClient,
) {
    fun sendInn(
        ettersending: EttersendelseDto,
        innsendingMottatt: LocalDateTime,
    ): Kvittering {
        val ettersedingRequestData: Map<StønadType, EttersendelseDto> = EttersendingMapper.groupByStønad(ettersending, innsendingMottatt)
        val kvittering = søknadClient.sendInnEttersending(ettersedingRequestData)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)
    }

    fun hentEttersendingForPerson(personIdent: String): List<EttersendelseDto> = søknadClient.hentEttersendingForPerson(personIdent)
}
