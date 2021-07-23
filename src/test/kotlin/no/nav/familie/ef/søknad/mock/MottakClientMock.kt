package no.nav.familie.ef.søknad.mock

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.api.dto.ettersending.EttersendingDto
import no.nav.familie.ef.søknad.api.dto.ettersending.EttersendingForSøknad
import no.nav.familie.ef.søknad.api.dto.ettersending.EttersendingUtenSøknad
import no.nav.familie.ef.søknad.api.dto.ettersending.Innsending
import no.nav.familie.ef.søknad.integration.SøknadClient
import no.nav.familie.ef.søknad.integration.dto.KvitteringDto
import no.nav.familie.kontrakter.ef.ettersending.SøknadMedDokumentasjonsbehovDto
import no.nav.familie.kontrakter.ef.felles.StønadType
import no.nav.familie.kontrakter.ef.søknad.Dokument
import no.nav.familie.kontrakter.ef.søknad.Dokumentasjonsbehov
import no.nav.familie.kontrakter.ef.søknad.SøknadType
import no.nav.familie.kontrakter.ef.søknad.dokumentasjonsbehov.DokumentasjonsbehovDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import java.time.LocalDate
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
        every { søknadClient.sendInnEttersending(any()) } returns KvitteringDto(("OK MOCK"))
        every { søknadClient.ping() } returns Unit
        every { søknadClient.hentDokumentasjonsbehovForSøknad(any()) } returns dokumentasjonsbehovDto
        every { søknadClient.hentSøknaderMedDokumentasjonsbehov(any()) } returns søknaderMedDokumentasjonsbehov
        every { søknadClient.hentEttersendingForPerson(any()) } returns ettersendingDto;


        return søknadClient
    }


    private val dokumentasjonsbehovDto =
            DokumentasjonsbehovDto(dokumentasjonsbehov = listOf(Dokumentasjonsbehov(
                    "Arbeidskontrakt som viser at du har fått tilbud om arbeid.",
                    "ARBEIDSKONTRAKT",
                    false,
                    listOf(Dokument("4648edd4-25bf-4881-b346-9edc6c1a5b4d", "dokument_soknad_2.pdf"))),
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

    private val dokumentasjonsbehovDto2 =
            DokumentasjonsbehovDto(dokumentasjonsbehov = listOf(
                    Dokumentasjonsbehov(
                            "Arbeidskontrakt som viser at du har fått tilbud om arbeid.",
                            "ARBEIDSKONTRAKT",
                            false,
                            listOf(Dokument("4648edd4-25bf-4881-b346-9edc6c1a5b4d", "dummy-pdf_2.pdf"))),
                    Dokumentasjonsbehov("Dokumentasjon på at du mangler barnepass",
                                        "DOKUMENTASJON_BARNEPASS_MANGEL",
                                        false,
                                        emptyList()),
            ),
                                   LocalDateTime.now(),
                                   SøknadType.OVERGANGSSTØNAD,
                                   "12345678910")

    private val søknaderMedDokumentasjonsbehov = listOf(
            SøknadMedDokumentasjonsbehovDto("b017ccb7-43e6-4040-ab9b-aab2d0d4fe98",
                                            StønadType.OVERGANGSSTØNAD,
                                            LocalDate.now(),
                                            dokumentasjonsbehovDto),
            SøknadMedDokumentasjonsbehovDto(UUID.randomUUID()
                                                    .toString(),
                                            StønadType.BARNETILSYN,
                                            LocalDate.now(),
                                            dokumentasjonsbehovDto2),
    )

    private val ettersendingForSøknad = EttersendingForSøknad("b017ccb7-43e6-4040-ab9b-aab2d0d4fe98",
                                                              listOf(Dokumentasjonsbehov("Dokumentasjon på arbeidsforholdet og årsaken til at du reduserte arbeidstiden",
                                                                                         "ARBEIDSFORHOLD_REDUSERT_ARBEIDSTID",
                                                                                         false,
                                                                                         listOf(Dokument("e2943989-932a-40fc-a1f0-db912ea8ccce",
                                                                                                         "dokuemnt_tidliger_ettersending.pdf")))),
                                                              emptyList())

    private val ettersendingUtenSøknad = EttersendingUtenSøknad(StønadType.OVERGANGSSTØNAD,
                                                                listOf(Innsending("dette er et fint dokument",
                                                                                  "Dokumentasjon som beskriver grunnen til at du ikke kan ta ethvert arbeid",
                                                                                  Dokument("093aaa5e-0bd3-4580-9db1-a15e109b3cdb",
                                                                                           "dokuemnt_tidliger_ettersending.pdf"))))

    private val ettersendingDto =
            listOf(EttersendingDto("01010172272", ettersendingForSøknad, null), EttersendingDto("01010172272", null, ettersendingUtenSøknad))

}