package no.nav.familie.ef.søknad.mapper

import no.nav.familie.ef.søknad.mapper.kontrakt.AktivitetsMapper
import no.nav.familie.ef.søknad.mock.søknadDto
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class UnderUtdanningMapperTest {

    // Gitt
    private val søknadDto = søknadDto()

    // Når
    private val aktivitet = AktivitetsMapper.map(søknadDto)


    @Test
    fun `Map underUtdanning er mappet `() {
        Assertions.assertThat(aktivitet.underUtdanning).isNotNull()
    }

    @Test
    fun `Map arbeidsforhold sluttdato verdi `() {
        Assertions.assertThat(aktivitet.underUtdanning?.verdi?.utdanningEtterGrunnskolen?.verdi).isTrue()
    }


}