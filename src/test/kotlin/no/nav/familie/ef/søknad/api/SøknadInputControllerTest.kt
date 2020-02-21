package no.nav.familie.ef.søknad.api

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Person
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
import no.nav.familie.ef.søknad.mock.søkerMedDefaultVerdier
import no.nav.familie.ef.søknad.mock.søknadDto
import no.nav.familie.ef.søknad.service.SøknadService
import no.nav.familie.kontrakter.felles.objectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SøknadInputControllerTest {

    private val søknadService: SøknadService = mockk()
    private val featureToggleService: FeatureToggleService = mockk()

    private val søknadController = SøknadController(søknadService, featureToggleService)

    @Test
    fun `sendInn returnerer samme kvittering som returneres fra søknadService`() {
        val søknad = søknadDto().copy(person = Person(søker = søkerMedDefaultVerdier(), barn = listOf()))
        every { søknadService.sendInn(søknad) } returns Kvittering("Mottatt søknad: ${søknad}")
        every { featureToggleService.isEnabled(any()) } returns true

        val kvitteringDto = søknadController.sendInn(søknad)

        assertThat(kvitteringDto.text).isEqualTo("Mottatt søknad: ${søknad}")
    }


    @Test
    fun `Deserialisering av eksempel json string fra ui skal ikke kaste exception`() {
        objectMapper.readValue(testData, SøknadDto::class.java)
    }

    val testData: String =
            """{"person":{"søker":{"fnr":"28129124085","forkortetNavn":"KOPP LUNKEN","adresse":{"adresse":"JAABÆKVEGEN 16","postnummer":"1533"},"egenansatt":false,"sivilstand":"UGIF","statsborgerskap":"NOR"},"barn":[{"fnr":"28021078036","navn":"KARAFFEL STERK","alder":9,"fødselsdato":"2010-02-28","harSammeAdresse":true}]},"sivilstatus":{},"medlemskap":{},"bosituasjon":{"søkerDelerBoligMedAndreVoksne":{"label":"","verdi":""}},"vedleggsliste":[]}"""


}
