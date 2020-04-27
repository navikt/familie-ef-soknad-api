package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Arbeidssøker
import no.nav.familie.ef.søknad.integration.SøknadClient
import no.nav.familie.ef.søknad.mapper.KvitteringMapper
import no.nav.familie.ef.søknad.mapper.SkjemaMapper
import org.springframework.stereotype.Service


@Service
internal class SkjemaServiceImpl(private val søknadClient: SøknadClient, private val skjemaMapper: SkjemaMapper) : SkjemaService {

    override fun sendInn(arbeidssøker: Arbeidssøker, fnr: String): Kvittering {
        val søknadDto = skjemaMapper.mapTilKontrakt(arbeidssøker, fnr)
        val kvittering = søknadClient.sendInnArbeidsRegistreringsskjema(søknadDto)
        return KvitteringMapper.mapTilEkstern(kvittering);

    }
}