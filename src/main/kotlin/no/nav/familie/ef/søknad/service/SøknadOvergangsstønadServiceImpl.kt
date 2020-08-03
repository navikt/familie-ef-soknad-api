package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadOvergangsstønadDto
import no.nav.familie.ef.søknad.integration.SøknadClient
import no.nav.familie.ef.søknad.mapper.KvitteringMapper
import no.nav.familie.ef.søknad.mapper.kontrakt.SøknadOvergangsstønadMapper
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
internal class SøknadOvergangsstønadServiceImpl(private val søknadClient: SøknadClient, private val mapper: SøknadOvergangsstønadMapper) : SøknadService {

    override fun sendInn(søknad: SøknadOvergangsstønadDto,
                         innsendingMottatt: LocalDateTime): Kvittering {
        val søknadRequestData = mapper.mapTilIntern(søknad, innsendingMottatt)
        val kvittering = søknadClient.sendInn(søknadRequestData)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)

    }
}