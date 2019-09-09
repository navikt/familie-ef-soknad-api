package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.api.dto.Søknad
import no.nav.familie.ef.søknad.integration.dto.SøknadDto

object SøknadMapper {

    fun mapTilIntern(søknad: Søknad): SøknadDto {
        return SøknadDto(søknad.text)
    }

}