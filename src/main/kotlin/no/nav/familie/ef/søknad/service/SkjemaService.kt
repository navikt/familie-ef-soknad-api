package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Arbeidssøker
import no.nav.familie.ef.søknad.integration.SøknadClient
import no.nav.familie.ef.søknad.integration.dto.KvitteringDto
import no.nav.familie.ef.søknad.mapper.SkjemaMapper
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SkjemaService(val søknadClient: SøknadClient) {

    fun sendInn(
        arbeidssøker: Arbeidssøker,
        fnr: String,
        navn: String,
        innsendingMottatt: LocalDateTime
    ): KvitteringDto {
        val søknadDto = SkjemaMapper.mapTilKontrakt(arbeidssøker, fnr, navn, innsendingMottatt)
        return søknadClient.sendInnArbeidsRegistreringsskjema(søknadDto)
    }
}
