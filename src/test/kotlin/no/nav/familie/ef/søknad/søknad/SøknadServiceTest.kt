package no.nav.familie.ef.søknad.søknad

import io.mockk.mockk
import no.nav.familie.ef.søknad.søknad.domain.Bosituasjon
import no.nav.familie.ef.søknad.søknad.domain.Medlemskap
import no.nav.familie.ef.søknad.søknad.domain.SivilstatusTilGjenbruk
import no.nav.familie.ef.søknad.søknad.dto.SøknadBarnetilsynDto
import no.nav.familie.ef.søknad.søknad.dto.SøknadBarnetilsynGjenbrukDto
import no.nav.familie.ef.søknad.søknad.mapper.SøknadBarnetilsynMapper
import no.nav.familie.ef.søknad.søknad.mapper.SøknadOvergangsstønadMapper
import no.nav.familie.ef.søknad.søknad.mapper.SøknadSkolepengerMapper
import no.nav.familie.kontrakter.ef.søknad.SøknadBarnetilsyn
import no.nav.familie.kontrakter.felles.objectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDateTime

class SøknadServiceTest {
    private val mottakClient = mockk<MottakClient>()

    private val søknadService =
        SøknadService(
            mottakClient = mottakClient,
            overgangsstønadMapper = SøknadOvergangsstønadMapper(),
            barnetilsynMapper = SøknadBarnetilsynMapper(),
            skolepengerMapper = SøknadSkolepengerMapper(),
        )

    @Test
    fun harSøknadGyldigeVerdier() {
        val søknadBT =
            objectMapper.readValue(
                File("src/test/resources/barnetilsyn/Barnetilsynsøknad.json"),
                SøknadBarnetilsyn::class.java,
            )

        val søknadTilGjenbruk = SøknadBarnetilsynMapper().mapTilDto(søknadBT)
        val skalHaGyldigeVerdier = søknadService.harSøknadGyldigeVerdier(søknadTilGjenbruk)
        assertThat(skalHaGyldigeVerdier).isTrue
    }
}
