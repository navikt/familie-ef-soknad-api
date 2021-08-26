package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadBarnetilsynDto
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadOvergangsstønadDto
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadSkolepengerDto
import no.nav.familie.ef.søknad.integration.SøknadClient
import no.nav.familie.ef.søknad.mapper.KvitteringMapper
import no.nav.familie.ef.søknad.mapper.kontrakt.SøknadBarnetilsynMapper
import no.nav.familie.ef.søknad.mapper.kontrakt.SøknadOvergangsstønadMapper
import no.nav.familie.ef.søknad.mapper.kontrakt.SøknadSkolepengerMapper
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SøknadService(private val søknadClient: SøknadClient,
                    private val overgangsstønadMapper: SøknadOvergangsstønadMapper,
                    private val barnetilsynMapper: SøknadBarnetilsynMapper,
                    private val skolepengerMapper: SøknadSkolepengerMapper) {

    fun sendInn(søknad: SøknadOvergangsstønadDto,
                innsendingMottatt: LocalDateTime): Kvittering {
        val søknadRequestData = overgangsstønadMapper.mapTilIntern(søknad, innsendingMottatt)
        val kvittering = søknadClient.sendInn(søknadRequestData)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)
    }

    fun sendInn(søknad: SøknadBarnetilsynDto, innsendingMottatt: LocalDateTime): Kvittering {
        val søknadRequestData = barnetilsynMapper.mapTilIntern(søknad, innsendingMottatt)
        val kvittering = søknadClient.sendInnBarnetilsynsøknad(søknadRequestData)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)
    }

    fun sendInn(søknad: SøknadSkolepengerDto, innsendingMottatt: LocalDateTime): Kvittering {
        val søknadRequestData = skolepengerMapper.mapTilIntern(søknad, innsendingMottatt)
        val kvittering = søknadClient.sendInnSkolepenger(søknadRequestData)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)
    }

}
