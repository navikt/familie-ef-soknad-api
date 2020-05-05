package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Arbeidssøker
import no.nav.familie.ef.søknad.integration.SøknadClient
import no.nav.familie.ef.søknad.mapper.KvitteringMapper
import no.nav.familie.ef.søknad.mapper.SkjemaMapper
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class SkjemaService(val søknadClient: SøknadClient)  {

    fun sendInn(arbeidssøker: Arbeidssøker,
                fnr: String,
                navn: String,
                innsendingMottatt: LocalDateTime): Kvittering {
        val søknadDto = SkjemaMapper.mapTilKontrakt(arbeidssøker, fnr, navn, innsendingMottatt)
        val kvittering = søknadClient.sendInnArbeidsRegistreringsskjema(søknadDto)
        return KvitteringMapper.mapTilEkstern(kvittering);

    }
}