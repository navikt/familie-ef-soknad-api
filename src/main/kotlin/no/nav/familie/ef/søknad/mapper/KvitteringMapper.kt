package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.integration.dto.KvitteringDto
import java.time.LocalDateTime

object KvitteringMapper {

    fun mapTilEkstern(
        kvitteringDto: KvitteringDto?,
        innsendingMottatt: LocalDateTime?
    ): Kvittering {
        return Kvittering(kvitteringDto?.text ?: "", innsendingMottatt)
    }
}
