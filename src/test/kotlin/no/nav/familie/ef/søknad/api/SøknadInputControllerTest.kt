package no.nav.familie.ef.søknad.api

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Person
import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
import no.nav.familie.ef.søknad.mock.søkerMedDefaultVerdier
import no.nav.familie.ef.søknad.mock.søknadDto
import no.nav.familie.ef.søknad.service.SøknadService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class SøknadInputControllerTest {

    private val søknadService: SøknadService = mockk()
    private val featureToggleService: FeatureToggleService = mockk()

    private val søknadController = SøknadOvergangsstønadController(søknadService, featureToggleService)

    @Test
    fun `sendInn returnerer kvittering riktig kvittering`() {

        val søknad = søknadDto().copy(person = Person(søker = søkerMedDefaultVerdier(), barn = listOf()))
        every { søknadService.sendInn(søknad, any()) } returns Kvittering("Mottatt søknad: $søknad", LocalDateTime.now())
        every { featureToggleService.isEnabled(any()) } returns true

        val kvitteringDto = søknadController.sendInn(søknad)

        assertThat(kvitteringDto.mottattDato).isNotNull()
    }

}
