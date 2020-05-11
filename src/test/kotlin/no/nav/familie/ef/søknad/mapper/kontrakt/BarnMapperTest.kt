package no.nav.familie.ef.søknad.mapper.kontrakt

import no.nav.familie.ef.søknad.mock.søknadDto
import org.junit.jupiter.api.Assertions.*

internal class BarnMapperTest {

    // Gitt
    private val søknadDto = søknadDto()

    // Når
    private val folkeregistrerteBarn = BarnMapper.mapFolkeregistrerteBarn(søknadDto.person.barn)
}