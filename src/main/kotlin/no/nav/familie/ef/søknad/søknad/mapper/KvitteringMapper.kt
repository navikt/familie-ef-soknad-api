package no.nav.familie.ef.søknad.søknad.mapper

import no.nav.familie.ef.søknad.søknad.domain.Kvittering
import no.nav.familie.ef.søknad.søknad.dto.KvitteringDto
import java.time.LocalDateTime

object KvitteringMapper {
    fun mapTilEkstern(
        kvitteringDto: KvitteringDto?,
        innsendingMottatt: LocalDateTime?,
    ): Kvittering = Kvittering(kvitteringDto?.text ?: "", innsendingMottatt)
}
