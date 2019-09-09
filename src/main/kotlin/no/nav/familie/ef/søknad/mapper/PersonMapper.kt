package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.Person
import no.nav.familie.ef.søknad.integration.dto.PersonDto

object PersonMapper {

    fun mapTilEkstern(personDto: PersonDto): Person {
        return Person(personDto.fnr,
                                                       personDto.fornavn,
                                                       personDto.etternavn,
                                                       personDto.mellomnavn,
                                                       personDto.kjønn,
                                                       personDto.fødselsdato,
                                                       personDto.bankkonto,
                                                       personDto.barn)
    }

}