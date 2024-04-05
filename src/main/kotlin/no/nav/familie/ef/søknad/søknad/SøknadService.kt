package no.nav.familie.ef.søknad.søknad

import no.nav.familie.ef.søknad.søknad.domain.Kvittering
import no.nav.familie.ef.søknad.søknad.dto.SøknadBarnetilsynDto
import no.nav.familie.ef.søknad.søknad.dto.SøknadBarnetilsynGjenbrukDto
import no.nav.familie.ef.søknad.søknad.dto.SøknadOvergangsstønadDto
import no.nav.familie.ef.søknad.søknad.dto.SøknadSkolepengerDto
import no.nav.familie.ef.søknad.søknad.mapper.KvitteringMapper
import no.nav.familie.ef.søknad.søknad.mapper.SøknadBarnetilsynMapper
import no.nav.familie.ef.søknad.søknad.mapper.SøknadOvergangsstønadMapper
import no.nav.familie.ef.søknad.søknad.mapper.SøknadSkolepengerMapper
import no.nav.familie.kontrakter.felles.objectMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SøknadService(
    private val søknadClient: SøknadClient,
    private val overgangsstønadMapper: SøknadOvergangsstønadMapper,
    private val barnetilsynMapper: SøknadBarnetilsynMapper,
    private val skolepengerMapper: SøknadSkolepengerMapper,
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    fun sendInn(
        søknad: SøknadOvergangsstønadDto,
        innsendingMottatt: LocalDateTime,
    ): Kvittering {
        val søknadRequestData = overgangsstønadMapper.mapTilIntern(søknad, innsendingMottatt)
        val kvittering = søknadClient.sendInn(søknadRequestData)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)
    }

    fun sendInn(søknad: SøknadBarnetilsynDto, innsendingMottatt: LocalDateTime): Kvittering {
        val søknadRequestData = barnetilsynMapper.mapTilIntern(søknad, innsendingMottatt)
        logger.info("SøknadBarnetilsynDto før den sendes til mottak: " + objectMapper.writeValueAsString(søknadRequestData))
        val kvittering = søknadClient.sendInnBarnetilsynsøknad(søknadRequestData)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)
    }

    fun sendInn(søknad: SøknadSkolepengerDto, innsendingMottatt: LocalDateTime): Kvittering {
        val søknadRequestData = skolepengerMapper.mapTilIntern(søknad, innsendingMottatt)
        val kvittering = søknadClient.sendInnSkolepenger(søknadRequestData)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)
    }

    fun hentForrigeBarnetilsynSøknad(): SøknadBarnetilsynGjenbrukDto? {
        return SøknadBarnetilsynMapper().mapTilDto(søknadClient.hentForrigeBarnetilsynSøknad())
    }
}
