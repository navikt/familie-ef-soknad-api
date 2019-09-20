package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.integration.dto.KvitteringDto

object KvitteringMapper {

    fun mapTilEkstern(kvitteringDto: KvitteringDto?): Kvittering {
        return Kvittering(kvitteringDto?.text ?: "")
    }

}