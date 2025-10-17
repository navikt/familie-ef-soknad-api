package no.nav.familie.ef.søknad.søknad

import io.mockk.mockk
import no.nav.familie.ef.søknad.søknad.domain.AnnenForelder
import no.nav.familie.ef.søknad.søknad.domain.PersonTilGjenbruk
import no.nav.familie.ef.søknad.søknad.domain.SvarId
import no.nav.familie.ef.søknad.søknad.mapper.SøknadBarnetilsynMapper
import no.nav.familie.ef.søknad.søknad.mapper.SøknadOvergangsstønadMapper
import no.nav.familie.ef.søknad.søknad.mapper.SøknadSkolepengerMapper
import no.nav.familie.kontrakter.ef.søknad.SøknadBarnetilsyn
import no.nav.familie.kontrakter.ef.søknad.Søknadsfelt
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
    fun `sjekk gyldige svarIds i barnetilsynsøknad - har bare gyldige verdier`() {
        val søknadBT =
            objectMapper.readValue(
                File("src/test/resources/barnetilsyn/Barnetilsynsøknad.json"),
                SøknadBarnetilsyn::class.java,
            )

        val søknadTilGjenbruk = SøknadBarnetilsynMapper().mapTilDto(søknadBT)
        val barn = søknadTilGjenbruk?.person?.barn?.first()
        val annenForelder = barn?.forelder
        val borAnnenForelderISammeHusMedGyldigSvarId =
            søknadTilGjenbruk
                ?.person
                ?.barn
                ?.first()
                ?.forelder
                ?.borAnnenForelderISammeHus
                ?.copy(svarid = SvarId.NEI.verdi)
        val barnMedGyldigSvarId = barn?.copy(forelder = annenForelder?.copy(borAnnenForelderISammeHus = borAnnenForelderISammeHusMedGyldigSvarId))!!
        val oppdatertSøknadTilGjenbruk = søknadTilGjenbruk.copy(person = PersonTilGjenbruk(barn = listOf(barnMedGyldigSvarId)))
        val skalHaGyldigeVerdier = søknadService.harSøknadGyldigeVerdier(oppdatertSøknadTilGjenbruk)
        assertThat(skalHaGyldigeVerdier).isTrue
    }

    @Test
    fun `sjekk gyldige svarIds i barnetilsynsøknad - borISammeHus mangler svarId (blir null og er derfor gyldig)`() {
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
