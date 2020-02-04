package no.nav.familie.ef.søknad.service

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Person
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Søker
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.ef.søknad.integration.SøknadClient
import no.nav.familie.ef.søknad.integration.dto.KvitteringDto
import no.nav.familie.kontrakter.ef.søknad.Søknad
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SøknadInputServiceImplTest {

    private val søknadClient: SøknadClient = mockk()

    private val søknadService = SøknadServiceImpl(søknadClient)

    private val syntetiskFnr = "25058521089"
    private val søknadDto = SøknadDto(person = Person(søker = Søker(fnr = syntetiskFnr)))
    private val kvittering = KvitteringDto("")

    @BeforeEach
    private fun init() {
        every { søknadClient.sendInn(any<Søknad>()) } returns kvittering
    }

    @Test
    fun `sendInn skal returnere kvitteringDto basert på kvittering fra søknadClient`() {
        val kvitteringDto = søknadService.sendInn(søknadDto)

        assertThat(kvitteringDto).isEqualTo(Kvittering(kvittering.text))
    }

}