package no.nav.familie.ef.søknad.mock

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.DokumentFelt
import no.nav.familie.ef.søknad.integration.SøknadClient
import no.nav.familie.ef.søknad.integration.dto.KvitteringDto
import no.nav.familie.kontrakter.ef.søknad.Dokument
import no.nav.familie.kontrakter.ef.søknad.Dokumentasjonsbehov
import no.nav.familie.kontrakter.ef.søknad.SøknadType
import no.nav.familie.kontrakter.ef.søknad.dokumentasjonsbehov.DokumentasjonsbehovDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import java.time.LocalDateTime
import java.util.UUID

@Configuration
@Profile("mock-mottak")
class MottakClientMock {


    @Bean
    @Primary
    fun søknadClient(): SøknadClient {
        val søknadClient: SøknadClient = mockk()

        every { søknadClient.sendInn(any()) } returns KvitteringDto("OK MOCK")
        every { søknadClient.sendInnArbeidsRegistreringsskjema(any()) } returns KvitteringDto("OK MOCK")
        every { søknadClient.sendInnBarnetilsynsøknad(any()) } returns KvitteringDto("OK MOCK")
        every { søknadClient.sendInnSkolepenger(any()) } returns KvitteringDto("OK MOCK")
        every { søknadClient.ping() } returns Unit
        every { søknadClient.hentDokumentasjonsbehovForSøknad(any()) } returns dokumentasjonsbehovDto
        every { søknadClient.hentSøknaderForPerson(any()) } returns listOf(UUID.randomUUID().toString())


        return søknadClient
    }

    private val dokumentasjonsbehovDto =
            DokumentasjonsbehovDto(dokumentasjonsbehov = listOf(Dokumentasjonsbehov(
                    "Arbeidskontrakt som viser at du har fått tilbud om arbeid.",
                    "ARBEIDSKONTRAKT",
                    false,
                    listOf(Dokument("4648edd4-25bf-4881-b346-9edc6c1a5b4d", "dummy-pdf_2.pdf"))),
                                                                Dokumentasjonsbehov("Dokumentasjon på barnets sykdom",
                                                                                    "SYKT_BARN",
                                                                                    false,
                                                                                    emptyList()),
                                                                Dokumentasjonsbehov("Dokumentasjon på arbeidsforholdet og årsaken til at du reduserte arbeidstiden",
                                                                                    "ARBEIDSFORHOLD_REDUSERT_ARBEIDSTID",
                                                                                    false,
                                                                                    emptyList())),
                                   LocalDateTime.now(),
                                   SøknadType.OVERGANGSSTØNAD,
                                   "12345678910")

}