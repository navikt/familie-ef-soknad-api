package no.nav.familie.ef.søknad.mock

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.ef.søknad.integration.SøknadClient
import no.nav.familie.ef.søknad.integration.dto.KvitteringDto
import no.nav.familie.kontrakter.ef.ettersending.EttersendingDto
import no.nav.familie.kontrakter.ef.ettersending.EttersendingForSøknad
import no.nav.familie.kontrakter.ef.ettersending.EttersendingResponseData
import no.nav.familie.kontrakter.ef.ettersending.EttersendingUtenSøknad
import no.nav.familie.kontrakter.ef.ettersending.Innsending
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
        every { søknadClient.sendInnEttersendingOld(any()) } returns KvitteringDto(("OK MOCK"))
        every { søknadClient.ping() } returns Unit
        every { søknadClient.hentDokumentasjonsbehovForSøknad(any()) } returns dokumentasjonsbehovDto
        every { søknadClient.hentSøknaderMedDokumentasjonsbehov(any()) } returns søknaderMedDokumentasjonsbehov
        every { søknadClient.hentEttersendingForPerson(any()) } returns ettersendingResponseData;


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
                                                              listOf(Innsending("Dette er et dokument",
                                                                                "DOKUMENTASJON_IKKE_VILLIG_TIL_ARBEID",
                                                                                Dokument("093aaa5e-0bd3-4580-9db1-a15e109b3cdb",
                                                                                         "dokuemnt_tidliger_ettersending22.pdf"))))

    private val ettersendingUtenSøknad = EttersendingUtenSøknad(listOf(Innsending("dette er et fint dokument",
                                                                                  "DOKUMENTASJON_IKKE_VILLIG_TIL_ARBEID",
                                                                                  Dokument("093aaa5e-0bd3-4580-9db1-a15e109b3cdb",
                                                                                           "dokuemnt_tidliger_ettersending.pdf"))))

    private val ettersendingDto1 =
            EttersendingDto("01010172272", StønadType.OVERGANGSSTØNAD, ettersendingForSøknad, null)

    private val ettersendingDto2 = EttersendingDto("01010172272", StønadType.BARNETILSYN, null, ettersendingUtenSøknad)

    private val ettersendingResponseData = listOf(EttersendingResponseData(ettersendingDto1, LocalDateTime.now()),
                                                  EttersendingResponseData(ettersendingDto2, LocalDateTime.now()))
}