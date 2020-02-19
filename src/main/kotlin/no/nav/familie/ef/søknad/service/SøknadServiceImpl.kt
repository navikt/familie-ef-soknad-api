package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.ef.søknad.integration.SøknadClient
import no.nav.familie.ef.søknad.mapper.KvitteringMapper
import no.nav.familie.ef.søknad.mapper.kontrakt.SøknadMapper
import org.springframework.stereotype.Service

@Service
internal class SøknadServiceImpl(private val søknadClient: SøknadClient, private val mapper: SøknadMapper) : SøknadService {

    override fun sendInn(søknad: SøknadDto): Kvittering {
        val søknadDto = mapper.mapTilIntern(søknad)
        val kvittering = søknadClient.sendInn(søknadDto)
        return KvitteringMapper.mapTilEkstern(kvittering)

    }
}