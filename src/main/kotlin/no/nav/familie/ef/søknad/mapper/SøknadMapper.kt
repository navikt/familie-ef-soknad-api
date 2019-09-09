package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.SøknadDto
import no.nav.familie.ef.søknad.integration.dto.Søknad

object SøknadMapper {

    fun mapTilIntern(søknadDto: SøknadDto): Søknad {
        return Søknad(søknadDto.text)
    }

}