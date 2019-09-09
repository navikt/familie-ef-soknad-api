package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.KvitteringDto
import no.nav.familie.ef.søknad.api.dto.SøknadDto
import no.nav.familie.ef.søknad.integration.SøknadClient
import no.nav.familie.ef.søknad.mapper.KvitteringMapper
import no.nav.familie.ef.søknad.mapper.SøknadMapper
import org.springframework.stereotype.Service

@Service
class SøknadServiceImpl(private val søknadClient: SøknadClient) : SøknadService {

    override fun sendInn(søknadDto: SøknadDto): KvitteringDto {

        val søknad = SøknadMapper.mapTilIntern(søknadDto)
        val kvittering = søknadClient.sendInn(søknad)
        return KvitteringMapper.mapTilEkstern(kvittering)

    }
}