package no.nav.familie.ef.soknad.service

import no.nav.familie.ef.soknad.api.dto.KvitteringDto
import no.nav.familie.ef.soknad.api.dto.SøknadDto
import no.nav.familie.ef.soknad.integration.SøknadClient
import no.nav.familie.ef.soknad.mapper.KvitteringMapper
import no.nav.familie.ef.soknad.mapper.SøknadMapper
import org.springframework.stereotype.Service

@Service
class SøknadServiceImpl(private val søknadClient: SøknadClient) : SøknadService {

    override fun sendInn(søknadDto: SøknadDto): KvitteringDto {

        val søknad = SøknadMapper.mapTilIntern(søknadDto)
        val kvittering = søknadClient.sendInn(søknad)
        return KvitteringMapper.mapTilEkstern(kvittering)

    }
}