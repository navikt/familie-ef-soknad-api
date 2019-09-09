package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.KvitteringDto
import no.nav.familie.ef.søknad.integration.dto.Kvittering

object KvitteringMapper {

    fun mapTilEkstern(kvittering: Kvittering?): KvitteringDto {
        return KvitteringDto(kvittering?.text ?: "")
    }

}