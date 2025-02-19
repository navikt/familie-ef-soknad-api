package no.nav.familie.ef.søknad.søknad

import no.nav.familie.ef.søknad.søknad.domain.Arbeidssøker
import no.nav.familie.ef.søknad.søknad.dto.KvitteringDto
import no.nav.familie.ef.søknad.søknad.mapper.SkjemaMapper
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SkjemaService(
    val søknadClient: MottakClient,
) {
    fun sendInn(
        arbeidssøker: Arbeidssøker,
        fnr: String,
        navn: String,
        innsendingMottatt: LocalDateTime,
    ): KvitteringDto {
        val søknadDto = SkjemaMapper.mapTilKontrakt(arbeidssøker, fnr, navn, innsendingMottatt)
        return søknadClient.sendInnArbeidsRegistreringsskjema(søknadDto)
    }
}
