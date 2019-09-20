package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.Søkerinfo
import no.nav.familie.ef.søknad.integration.dto.SøkerinfoDto

object SøkerinfoMapper {


    fun mapTilEkstern(søkerinfoDto: SøkerinfoDto): Søkerinfo {
        return Søkerinfo(PersonMapper.mapTilEkstern(søkerinfoDto.person),
                                                          søkerinfoDto.arbeidsforhold)

    }


}