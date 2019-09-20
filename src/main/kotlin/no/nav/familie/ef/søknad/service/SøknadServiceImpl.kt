package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.api.dto.Søknad
import no.nav.familie.ef.søknad.integration.SøknadClient
import no.nav.familie.ef.søknad.mapper.KvitteringMapper
import no.nav.familie.ef.søknad.mapper.SøknadMapper
import org.springframework.stereotype.Service

@Service
internal class SøknadServiceImpl(private val søknadClient: SøknadClient) : SøknadService {

    override fun sendInn(søknad: Søknad): Kvittering {

        val søknadDto = SøknadMapper.mapTilIntern(søknad)
        val kvittering = søknadClient.sendInn(søknadDto)
        return KvitteringMapper.mapTilEkstern(kvittering)

    }
}