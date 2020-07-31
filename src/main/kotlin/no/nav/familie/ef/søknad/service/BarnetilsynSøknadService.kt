package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.BarnetilsynDto
import no.nav.familie.ef.søknad.integration.SøknadClient
import no.nav.familie.ef.søknad.mapper.KvitteringMapper
import no.nav.familie.ef.søknad.mapper.kontrakt.BarnetilsynSøknadMapper
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class BarnetilsynSøknadService(val søknadClient: SøknadClient, private val søknadMapper: BarnetilsynSøknadMapper) {

    fun sendInn(barnetilsynDto: BarnetilsynDto, innsendingMottatt: LocalDateTime): Kvittering {
        val barnetilsynRequestData = søknadMapper.mapTilIntern(barnetilsynDto, innsendingMottatt)
        val kvittering = søknadClient.sendInnBarnetilsynsøknad(barnetilsynRequestData)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)
    }
}