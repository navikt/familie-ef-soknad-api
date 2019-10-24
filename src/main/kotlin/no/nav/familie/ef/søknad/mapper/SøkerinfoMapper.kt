package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.Søkerinfo
import no.nav.familie.ef.søknad.integration.dto.PersoninfoDto
import no.nav.familie.ef.søknad.integration.dto.RelasjonDto

object SøkerinfoMapper {

    fun mapTilSøkerinfo(personinfoDto: PersoninfoDto, aktuelleBarn: List<RelasjonDto>): Søkerinfo {
        return Søkerinfo(PersonMapper.mapTilPerson(personinfoDto),
                         aktuelleBarn.map(PersonMapper::mapTilBarn))
    }
}