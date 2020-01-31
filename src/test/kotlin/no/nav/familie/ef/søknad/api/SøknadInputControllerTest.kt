package no.nav.familie.ef.søknad.api

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadInput
import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
import no.nav.familie.ef.søknad.service.SøknadService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SøknadInputControllerTest {

    private val søknadService: SøknadService = mockk()
    private val featureToggleService: FeatureToggleService = mockk()

    private val søknadController = SøknadController(søknadService, featureToggleService )

    @Test
    fun `sendInn returnerer samme kvittering som returneres fra søknadService`() {
        val søknad = SøknadInput("tekst")
        every { søknadService.sendInn(søknad) } returns Kvittering("Mottatt søknad: ${søknad.text}")
        every { featureToggleService.isEnabled(any())} returns true

        val kvitteringDto = søknadController.sendInn(søknad)

        assertThat(kvitteringDto.text).isEqualTo("Mottatt søknad: ${søknad.text}")
    }
}