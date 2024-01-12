package no.nav.familie.ef.søknad.api

import io.mockk.mockk
import no.nav.familie.ef.søknad.integrationTest.OppslagSpringRunnerTest
import no.nav.familie.ef.søknad.søknad.SøknadClient
import no.nav.familie.ef.søknad.søknad.SøknadClientUtil.filtrerVekkEldreDokumentasjonsbehov
import no.nav.familie.kontrakter.ef.ettersending.SøknadMedDokumentasjonsbehovDto
import no.nav.familie.kontrakter.ef.søknad.SøknadType
import no.nav.familie.kontrakter.ef.søknad.dokumentasjonsbehov.DokumentasjonsbehovDto
import no.nav.familie.kontrakter.felles.ef.StønadType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate
import java.util.UUID

@Profile("dokumentasjonsbehov-test")
@Configuration
class DokumentasjonsbehovControllerTestConfiguration {

    @Primary
    @Bean
    fun søknadClient(): SøknadClient = mockk()
}

@ActiveProfiles("dokumentasjonsbehov-test")
internal class DokumentasjonsbehovControllerTest : OppslagSpringRunnerTest() {

    @Autowired
    lateinit var søknadClient: SøknadClient

    val tokenSubject = "12345678911"

    @BeforeEach
    fun førAlle() {
        headers.setBearerAuth(søkerBearerToken(tokenSubject))
    }

    @Test
    internal fun `dokumentasjonsbehov som er eldre enn 28 dager (4 uker) skal ikke hentes`() {
        val eldreSøknad: SøknadMedDokumentasjonsbehovDto = lagSøknadMedDokumentasjonsbehov(
            fødselsnummer = "0",
            innsendtDato = LocalDate.of(
                2021,
                10,
                5,
            ),
        )
        val nySøknad: SøknadMedDokumentasjonsbehovDto = lagSøknadMedDokumentasjonsbehov(
            fødselsnummer = "0",
            innsendtDato = LocalDate.now(),
        )
        val søknader: List<SøknadMedDokumentasjonsbehovDto> = listOf(eldreSøknad, nySøknad)
        val filtrerteSøknader: List<SøknadMedDokumentasjonsbehovDto> = filtrerVekkEldreDokumentasjonsbehov(søknader)

        assertThat(filtrerteSøknader).isEqualTo(listOf(nySøknad))
    }

    private fun lagDokumentasjonsbehov(fødselsnummer: String, innsendtDato: LocalDate): DokumentasjonsbehovDto =
        DokumentasjonsbehovDto(
            emptyList(),
            innsendtDato.atTime(0, 0),
            SøknadType.OVERGANGSSTØNAD,
            fødselsnummer,
        )

    private fun lagSøknadMedDokumentasjonsbehov(
        søknadId: String = UUID.randomUUID().toString(),
        fødselsnummer: String,
        innsendtDato: LocalDate,
    ): SøknadMedDokumentasjonsbehovDto =
        SøknadMedDokumentasjonsbehovDto(
            søknadId,
            StønadType.OVERGANGSSTØNAD,
            innsendtDato,
            lagDokumentasjonsbehov(fødselsnummer, innsendtDato),
        )
}
