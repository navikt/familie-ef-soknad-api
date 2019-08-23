package no.nav.familie.ef.soknad.mapper

import no.nav.familie.ef.soknad.api.dto.KvitteringDto
import no.nav.familie.ef.soknad.integration.dto.Kvittering

object KvitteringMapper {

    fun mapTilEkstern(kvittering: Kvittering?): KvitteringDto {
        return KvitteringDto(kvittering?.text ?: "")
    }

}