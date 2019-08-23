package no.nav.familie.ef.soknad.mapper

import no.nav.familie.ef.soknad.api.dto.SøknadDto
import no.nav.familie.ef.soknad.integration.dto.Søknad

object SøknadMapper {

    fun mapTilIntern(søknadDto: SøknadDto): Søknad {
        return Søknad(søknadDto.text)
    }

}