package no.nav.familie.ef.søknad.minside

import com.fasterxml.jackson.module.kotlin.readValue
import io.mockk.every
import io.mockk.mockk
import no.nav.familie.kontrakter.felles.objectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile

@Configuration
@Profile("mock-saf")
class SafClientConfig {

    @Bean
    @Primary
    fun safClient(): SafClient {
        val safClient: SafClient = mockk()

        every { safClient.ping() } returns Unit

        every { safClient.hentJournalposterForBruker(any()) } returns
            objectMapper.readValue(dokumenter)

        return safClient
    }

    val dokumenter = """{
	
		"dokumentoversiktSelvbetjening": {
			"tema": [{
				"navn": "Enslig mor eller far",
				"kode": "ENF",
				"journalposter": [{
					"tittel": "VANL Brev",
					"journalpostId": "453858397",
					"journalposttype": "I",
					"journalstatus": "MOTTATT",
					"avsender": null,
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2024-01-24T12:37:27",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2024-01-24T12:37:27",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2024-01-24T12:37:27",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "VANL Brev",
						"dokumentInfoId": "454251345",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["ingen_partsinnsyn", "skannet_dokument"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "vedlegg: 1",
						"dokumentInfoId": "454251346",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["ingen_partsinnsyn", "skannet_dokument"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "VANL Brev",
					"journalpostId": "453858396",
					"journalposttype": "I",
					"journalstatus": "MOTTATT",
					"avsender": null,
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2024-01-24T12:37:22",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2024-01-24T12:37:22",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2024-01-24T12:37:22",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "VANL Brev",
						"dokumentInfoId": "454251344",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["ingen_partsinnsyn", "skannet_dokument"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "Ettersending til søknad om OVERGANGSSTØNAD",
					"journalpostId": "453858394",
					"journalposttype": "I",
					"journalstatus": "MOTTATT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2024-01-24T12:34:19",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2024-01-24T12:34:19",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2024-01-24T12:34:19",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "Ettersending overgangsstønad - enslig mor eller far",
						"dokumentInfoId": "454251341",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "Lærlingkontrakt",
						"dokumentInfoId": "454251342",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "Vedtak om avslått overgangstønad",
					"journalpostId": "453856376",
					"journalposttype": "U",
					"journalstatus": "FERDIGSTILT",
					"avsender": null,
					"mottaker": {
						"id": "08909298858",
						"type": "FNR"
					},
					"relevanteDatoer": [{
						"dato": "2024-01-05T09:42:14",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2024-01-05T09:42:14",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2024-01-05T09:42:14",
						"datotype": "DATO_JOURNALFOERT"
					}],
					"dokumenter": [{
						"tittel": "Vedtak om avslått overgangstønad",
						"dokumentInfoId": "454249083",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "Søknad om overgangsstønad",
					"journalpostId": "453856260",
					"journalposttype": "I",
					"journalstatus": "MOTTATT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2024-01-04T13:34:15",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2024-01-04T13:34:15",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2024-01-04T13:34:15",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "Søknad om overgangsstønad - enslig mor eller far",
						"dokumentInfoId": "454248906",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "Søknad om overgangsstønad",
					"journalpostId": "453856000",
					"journalposttype": "I",
					"journalstatus": "MOTTATT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2024-01-02T14:19:48",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2024-01-02T14:19:48",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2024-01-02T14:19:48",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "Søknad om overgangsstønad - enslig mor eller far",
						"dokumentInfoId": "454248589",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "Dokumentasjon på barnets sykdom",
						"dokumentInfoId": "454248590",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "Søknad om overgangsstønad",
					"journalpostId": "453855993",
					"journalposttype": "I",
					"journalstatus": "MOTTATT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2024-01-02T13:56:25",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2024-01-02T13:56:25",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2024-01-02T13:56:25",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "Søknad om overgangsstønad - enslig mor eller far",
						"dokumentInfoId": "454248581",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "Dokumentasjon på separasjon eller skilsmisse",
						"dokumentInfoId": "454248582",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "VANL Annet skjema (ikke NAV-skjema)",
					"journalpostId": "453855663",
					"journalposttype": "I",
					"journalstatus": "MOTTATT",
					"avsender": null,
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-12-21T12:35:32",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-12-21T12:35:32",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-12-21T12:35:32",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "VANL Annet skjema (ikke NAV-skjema)",
						"dokumentInfoId": "454248195",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["ingen_partsinnsyn", "skannet_dokument"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "vedlegg: 1",
						"dokumentInfoId": "454248196",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["ingen_partsinnsyn", "skannet_dokument"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "vedlegg: 2",
						"dokumentInfoId": "454248197",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["ingen_partsinnsyn", "skannet_dokument"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "VANL Annet skjema (ikke NAV-skjema)",
					"journalpostId": "453855662",
					"journalposttype": "I",
					"journalstatus": "MOTTATT",
					"avsender": null,
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-12-21T12:35:31",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-12-21T12:35:31",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-12-21T12:35:31",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "VANL Annet skjema (ikke NAV-skjema)",
						"dokumentInfoId": "454248192",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["ingen_partsinnsyn", "skannet_dokument"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "vedlegg: 1",
						"dokumentInfoId": "454248193",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["ingen_partsinnsyn", "skannet_dokument"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "vedlegg: 2",
						"dokumentInfoId": "454248194",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["ingen_partsinnsyn", "skannet_dokument"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "VANL Annet skjema (ikke NAV-skjema)",
					"journalpostId": "453855661",
					"journalposttype": "I",
					"journalstatus": "MOTTATT",
					"avsender": null,
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-12-21T12:35:30",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-12-21T12:35:30",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-12-21T12:35:30",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "VANL Annet skjema (ikke NAV-skjema)",
						"dokumentInfoId": "454248189",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["ingen_partsinnsyn", "skannet_dokument"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "vedlegg: 1",
						"dokumentInfoId": "454248190",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["ingen_partsinnsyn", "skannet_dokument"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "vedlegg: 2",
						"dokumentInfoId": "454248191",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["ingen_partsinnsyn", "skannet_dokument"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "VANL Annet skjema (ikke NAV-skjema)",
					"journalpostId": "453855660",
					"journalposttype": "I",
					"journalstatus": "MOTTATT",
					"avsender": null,
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-12-21T12:35:29",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-12-21T12:35:29",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-12-21T12:35:29",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "VANL Annet skjema (ikke NAV-skjema)",
						"dokumentInfoId": "454248186",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["ingen_partsinnsyn", "skannet_dokument"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "vedlegg: 1",
						"dokumentInfoId": "454248187",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["ingen_partsinnsyn", "skannet_dokument"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "vedlegg: 2",
						"dokumentInfoId": "454248188",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["ingen_partsinnsyn", "skannet_dokument"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "NAV 15-00.04 Søknad om stønad til skolepenger - enslig mor eller far",
					"journalpostId": "453855659",
					"journalposttype": "I",
					"journalstatus": "JOURNALFOERT",
					"avsender": null,
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-12-21T12:35:13",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-12-21T12:35:13",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-12-21T13:26:15",
						"datotype": "DATO_JOURNALFOERT"
					}, {
						"dato": "2023-12-21T12:35:13",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "NAV 15-00.04 Søknad om stønad til skolepenger - enslig mor eller far",
						"dokumentInfoId": "454248183",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["ingen_partsinnsyn", "skannet_dokument"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "vedlegg: 1",
						"dokumentInfoId": "454248184",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["ingen_partsinnsyn", "skannet_dokument"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "vedlegg: 2",
						"dokumentInfoId": "454248185",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["ingen_partsinnsyn", "skannet_dokument"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "NAV 15-00.04 Søknad om stønad til skolepenger - enslig mor eller far",
					"journalpostId": "453855657",
					"journalposttype": "I",
					"journalstatus": "JOURNALFOERT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-12-21T12:34:59",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-12-21T12:34:59",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-12-21T12:43:27",
						"datotype": "DATO_JOURNALFOERT"
					}, {
						"dato": "2023-12-21T12:34:59",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "NAV 15-00.04 Søknad om stønad til skolepenger - enslig mor eller far",
						"dokumentInfoId": "454248179",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["skannet_dokument"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "NAV 15-00.04 Søknad om stønad til skolepenger - enslig mor eller far",
					"journalpostId": "453855656",
					"journalposttype": "I",
					"journalstatus": "JOURNALFOERT",
					"avsender": null,
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-12-21T12:34:58",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-12-21T12:34:58",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-12-21T12:41:30",
						"datotype": "DATO_JOURNALFOERT"
					}, {
						"dato": "2023-12-21T12:34:58",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "NAV 15-00.04 Søknad om stønad til skolepenger - enslig mor eller far",
						"dokumentInfoId": "454248178",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["ingen_partsinnsyn", "skannet_dokument"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "Søknad om stønad til barnetilsyn",
					"journalpostId": "453854687",
					"journalposttype": "I",
					"journalstatus": "JOURNALFOERT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-12-12T09:46:03",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-12-12T09:46:03",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-12-12T09:47:59",
						"datotype": "DATO_JOURNALFOERT"
					}, {
						"dato": "2023-12-12T09:46:03",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "Søknad om stønad til barnetilsyn - enslig mor eller far i arbeid",
						"dokumentInfoId": "454246908",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "Avtalen du har med barnepasseren",
						"dokumentInfoId": "454246909",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "Søknad om overgangsstønad",
					"journalpostId": "453854160",
					"journalposttype": "I",
					"journalstatus": "MOTTATT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-12-06T15:16:41",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-12-06T15:16:41",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-12-06T15:16:41",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "Søknad om overgangsstønad - enslig mor eller far",
						"dokumentInfoId": "454246035",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "Søknad om stønad til barnetilsyn",
					"journalpostId": "453853293",
					"journalposttype": "I",
					"journalstatus": "MOTTATT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-11-29T09:13:13",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-29T09:13:13",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-29T09:13:13",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "Søknad om stønad til barnetilsyn - enslig mor eller far i arbeid",
						"dokumentInfoId": "454244967",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "Dokumentasjon på adresseendring",
						"dokumentInfoId": "454244968",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "Dokumentasjon på at du er syk",
						"dokumentInfoId": "454244969",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "Dokumentasjon på at du er syk",
						"dokumentInfoId": "454244970",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "Søknad om stønad til barnetilsyn",
					"journalpostId": "453853292",
					"journalposttype": "I",
					"journalstatus": "JOURNALFOERT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-11-29T09:08:59",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-29T09:08:59",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-12-11T09:00:05",
						"datotype": "DATO_JOURNALFOERT"
					}, {
						"dato": "2023-11-29T09:08:59",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "Søknad om stønad til barnetilsyn - enslig mor eller far i arbeid",
						"dokumentInfoId": "454244964",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "Næringsfaglig vurdering av virksomheten du etablerer",
						"dokumentInfoId": "454244965",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "Faktura fra barnepassordningen for perioden du søker om nå",
						"dokumentInfoId": "454244966",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "Vedtak om innvilget overgangstønad",
					"journalpostId": "453853070",
					"journalposttype": "U",
					"journalstatus": "FERDIGSTILT",
					"avsender": null,
					"mottaker": {
						"id": "08909298858",
						"type": "FNR"
					},
					"relevanteDatoer": [{
						"dato": "2023-11-27T14:24:23",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-27T14:24:23",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-27T14:24:23",
						"datotype": "DATO_JOURNALFOERT"
					}],
					"dokumenter": [{
						"tittel": "Vedtak om innvilget overgangstønad",
						"dokumentInfoId": "454244684",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "Varsel tilbakebetaling Overgangsstønad",
					"journalpostId": "453853069",
					"journalposttype": "U",
					"journalstatus": "FERDIGSTILT",
					"avsender": null,
					"mottaker": {
						"id": "08909298858",
						"type": "FNR"
					},
					"relevanteDatoer": [{
						"dato": "2023-11-27T14:24:07",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-27T14:24:07",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-27T14:24:07",
						"datotype": "DATO_JOURNALFOERT"
					}],
					"dokumenter": [{
						"tittel": "Varsel tilbakebetaling Overgangsstønad",
						"dokumentInfoId": "454244683",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "Søknad om stønad til barnetilsyn",
					"journalpostId": "453853021",
					"journalposttype": "I",
					"journalstatus": "JOURNALFOERT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-11-27T10:53:11",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-27T10:53:11",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-27T10:53:25",
						"datotype": "DATO_JOURNALFOERT"
					}, {
						"dato": "2023-11-27T10:53:11",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "Søknad om stønad til barnetilsyn - enslig mor eller far i arbeid",
						"dokumentInfoId": "454244626",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "Søknad om overgangsstønad",
					"journalpostId": "453853020",
					"journalposttype": "I",
					"journalstatus": "JOURNALFOERT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-11-27T10:52:12",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-27T10:52:12",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-27T10:55:23",
						"datotype": "DATO_JOURNALFOERT"
					}, {
						"dato": "2023-11-27T10:52:12",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "Søknad om overgangsstønad - enslig mor eller far",
						"dokumentInfoId": "454244624",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "Terminbekreftelse",
						"dokumentInfoId": "454244625",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "Brev om oversendelse til NAV Klageinstans - overgangsstønad",
					"journalpostId": "453852878",
					"journalposttype": "U",
					"journalstatus": "FERDIGSTILT",
					"avsender": null,
					"mottaker": {
						"id": "811035149",
						"type": "ORGNR"
					},
					"relevanteDatoer": [{
						"dato": "2023-11-24T08:35:59",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-24T08:35:59",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-24T08:35:59",
						"datotype": "DATO_JOURNALFOERT"
					}],
					"dokumenter": [{
						"tittel": "Brev om oversendelse til NAV Klageinstans - overgangsstønad",
						"dokumentInfoId": "454244470",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["ingen_partsinnsyn"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "Brev om oversendelse til NAV Klageinstans - overgangsstønad",
					"journalpostId": "453852877",
					"journalposttype": "U",
					"journalstatus": "FERDIGSTILT",
					"avsender": null,
					"mottaker": {
						"id": "08909298858",
						"type": "FNR"
					},
					"relevanteDatoer": [{
						"dato": "2023-11-24T08:35:59",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-24T08:35:59",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-24T08:35:59",
						"datotype": "DATO_JOURNALFOERT"
					}],
					"dokumenter": [{
						"tittel": "Brev om oversendelse til NAV Klageinstans - overgangsstønad",
						"dokumentInfoId": "454244469",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "Brev om oversendelse til NAV Klageinstans - stønad til barnetilsyn",
					"journalpostId": "453852834",
					"journalposttype": "U",
					"journalstatus": "FERDIGSTILT",
					"avsender": null,
					"mottaker": {
						"id": "08909298858",
						"type": "FNR"
					},
					"relevanteDatoer": [{
						"dato": "2023-11-23T16:16:35",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-23T16:16:35",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-23T16:16:35",
						"datotype": "DATO_JOURNALFOERT"
					}],
					"dokumenter": [{
						"tittel": "Brev om oversendelse til NAV Klageinstans - stønad til barnetilsyn",
						"dokumentInfoId": "454244428",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "Ettersending til søknad om SKOLEPENGER",
					"journalpostId": "453852712",
					"journalposttype": "I",
					"journalstatus": "MOTTATT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-11-22T16:28:23",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-22T16:28:23",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-22T16:28:23",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "Ettersending skolepenger - enslig mor eller far",
						"dokumentInfoId": "454244280",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "Avtale om delt fast bosted",
						"dokumentInfoId": "454244281",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "Bekreftelse på samlivsbrudd med den andre forelderen",
						"dokumentInfoId": "454244282",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "NAV 15-00.02 Søknad om stønad til barnetilsyn - enslig mor eller far i arbeid",
					"journalpostId": "453852452",
					"journalposttype": "I",
					"journalstatus": "JOURNALFOERT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-11-21T11:12:16",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-21T11:12:16",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-21T11:22:58",
						"datotype": "DATO_JOURNALFOERT"
					}, {
						"dato": "2023-11-21T11:12:16",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "NAV 15-00.02 Søknad om stønad til barnetilsyn - enslig mor eller far i arbeid",
						"dokumentInfoId": "454243992",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["skannet_dokument"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "NAV 15-00.02 Søknad om stønad til barnetilsyn - enslig mor eller far i arbeid",
					"journalpostId": "453852451",
					"journalposttype": "I",
					"journalstatus": "JOURNALFOERT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-11-21T11:12:15",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-21T11:12:15",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-12-11T23:22:01",
						"datotype": "DATO_JOURNALFOERT"
					}, {
						"dato": "2023-11-21T11:12:15",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "NAV 15-00.02 Søknad om stønad til barnetilsyn - enslig mor eller far i arbeid",
						"dokumentInfoId": "454243991",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["skannet_dokument"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "NAV 90-00.08 K Klage",
					"journalpostId": "453852449",
					"journalposttype": "I",
					"journalstatus": "JOURNALFOERT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-11-21T11:12:11",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-21T11:12:11",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-21T11:21:19",
						"datotype": "DATO_JOURNALFOERT"
					}, {
						"dato": "2023-11-21T11:12:11",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "NAV 90-00.08 K Klage",
						"dokumentInfoId": "454243989",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["skannet_dokument"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "NAV 90-00.08 K Klage",
					"journalpostId": "453852448",
					"journalposttype": "I",
					"journalstatus": "JOURNALFOERT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-11-21T11:12:09",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-21T11:12:09",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-21T11:23:48",
						"datotype": "DATO_JOURNALFOERT"
					}, {
						"dato": "2023-11-21T11:12:09",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "NAV 90-00.08 K Klage",
						"dokumentInfoId": "454243988",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["skannet_dokument"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "NAV 90-00.08 K Klage",
					"journalpostId": "453852447",
					"journalposttype": "I",
					"journalstatus": "JOURNALFOERT",
					"avsender": null,
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-11-21T11:12:08",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-21T11:12:08",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-12-21T12:25:15",
						"datotype": "DATO_JOURNALFOERT"
					}, {
						"dato": "2023-11-21T11:12:08",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "NAV 90-00.08 K Klage",
						"dokumentInfoId": "454243987",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["ingen_partsinnsyn", "skannet_dokument"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "NAV 90-00.08 K Klage",
					"journalpostId": "453852446",
					"journalposttype": "I",
					"journalstatus": "JOURNALFOERT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-11-21T11:12:07",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-21T11:12:07",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-21T11:25:13",
						"datotype": "DATO_JOURNALFOERT"
					}, {
						"dato": "2023-11-21T11:12:07",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "NAV 90-00.08 K Klage",
						"dokumentInfoId": "454243986",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["skannet_dokument"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "NAV 90-00.08 K Klage",
					"journalpostId": "453852236",
					"journalposttype": "I",
					"journalstatus": "JOURNALFOERT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-11-20T10:35:15",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-20T10:35:15",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-21T11:11:46",
						"datotype": "DATO_JOURNALFOERT"
					}, {
						"dato": "2023-11-20T10:35:15",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "NAV 90-00.08 K Klage",
						"dokumentInfoId": "454243731",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["skannet_dokument"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "NAV 15-00.01 Søknad om overgangsstønad - enslig mor eller far",
					"journalpostId": "453852235",
					"journalposttype": "I",
					"journalstatus": "JOURNALFOERT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-11-20T10:35:11",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-20T10:35:11",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-20T10:52:59",
						"datotype": "DATO_JOURNALFOERT"
					}, {
						"dato": "2023-11-20T10:35:11",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "NAV 15-00.01 Søknad om overgangsstønad - enslig mor eller far",
						"dokumentInfoId": "454243730",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["skannet_dokument"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "NAV 90-00.08 K Klage",
					"journalpostId": "453852234",
					"journalposttype": "I",
					"journalstatus": "JOURNALFOERT",
					"avsender": null,
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-11-20T10:35:06",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-20T10:35:06",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-20T10:48:40",
						"datotype": "DATO_JOURNALFOERT"
					}, {
						"dato": "2023-11-20T10:35:06",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "NAV 90-00.08 K Klage",
						"dokumentInfoId": "454243729",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["ingen_partsinnsyn", "skannet_dokument"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "Vedtak om innvilget overgangstønad",
					"journalpostId": "453851788",
					"journalposttype": "U",
					"journalstatus": "FERDIGSTILT",
					"avsender": null,
					"mottaker": {
						"id": "08909298858",
						"type": "FNR"
					},
					"relevanteDatoer": [{
						"dato": "2023-11-16T11:05:12",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-16T11:05:12",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-16T11:05:12",
						"datotype": "DATO_JOURNALFOERT"
					}],
					"dokumenter": [{
						"tittel": "Vedtak om innvilget overgangstønad",
						"dokumentInfoId": "454243197",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "Søknad om overgangsstønad",
					"journalpostId": "453851782",
					"journalposttype": "I",
					"journalstatus": "JOURNALFOERT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-11-16T10:55:28",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-16T10:55:28",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-16T11:33:31",
						"datotype": "DATO_JOURNALFOERT"
					}, {
						"dato": "2023-11-16T10:55:28",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "Søknad om overgangsstønad - enslig mor eller far",
						"dokumentInfoId": "454243188",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "Dokumentasjon på barnets sykdom",
						"dokumentInfoId": "454243189",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "Dokumentasjon på barnets sykdom",
						"dokumentInfoId": "454243190",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "Dokumentasjon på barnets sykdom",
						"dokumentInfoId": "454243191",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "NAV 15-00.02 Søknad om stønad til barnetilsyn - enslig mor eller far i arbeid",
					"journalpostId": "453851780",
					"journalposttype": "I",
					"journalstatus": "JOURNALFOERT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-11-16T10:34:31",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-16T10:34:31",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-21T11:09:04",
						"datotype": "DATO_JOURNALFOERT"
					}, {
						"dato": "2023-11-16T10:34:31",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "NAV 15-00.02 Søknad om stønad til barnetilsyn - enslig mor eller far i arbeid",
						"dokumentInfoId": "454243185",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["skannet_dokument"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "vedlegg: 1",
						"dokumentInfoId": "454243186",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["skannet_dokument"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "NAV 15-00.02 Søknad om stønad til barnetilsyn - enslig mor eller far i arbeid",
					"journalpostId": "453851779",
					"journalposttype": "I",
					"journalstatus": "JOURNALFOERT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-11-16T10:34:30",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-16T10:34:30",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-16T10:39:23",
						"datotype": "DATO_JOURNALFOERT"
					}, {
						"dato": "2023-11-16T10:34:30",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "NAV 15-00.02 Søknad om stønad til barnetilsyn - enslig mor eller far i arbeid",
						"dokumentInfoId": "454243183",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["skannet_dokument"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "vedlegg: 1",
						"dokumentInfoId": "454243184",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["skannet_dokument"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "NAV 15-00.02 Søknad om stønad til barnetilsyn - enslig mor eller far i arbeid",
					"journalpostId": "453851778",
					"journalposttype": "I",
					"journalstatus": "JOURNALFOERT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-11-16T10:34:29",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-16T10:34:29",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-20T10:51:13",
						"datotype": "DATO_JOURNALFOERT"
					}, {
						"dato": "2023-11-16T10:34:29",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "NAV 15-00.02 Søknad om stønad til barnetilsyn - enslig mor eller far i arbeid",
						"dokumentInfoId": "454243181",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["skannet_dokument"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "vedlegg: 1",
						"dokumentInfoId": "454243182",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["skannet_dokument"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "NAV 90-00.08 K Klage",
					"journalpostId": "453851777",
					"journalposttype": "I",
					"journalstatus": "JOURNALFOERT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-11-16T10:34:26",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-16T10:34:26",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-17T13:54:24",
						"datotype": "DATO_JOURNALFOERT"
					}, {
						"dato": "2023-11-16T10:34:26",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "NAV 90-00.08 K Klage",
						"dokumentInfoId": "454243179",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["skannet_dokument"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "vedlegg: 1",
						"dokumentInfoId": "454243180",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["skannet_dokument"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "NAV 90-00.08 K Klage",
					"journalpostId": "453851776",
					"journalposttype": "I",
					"journalstatus": "JOURNALFOERT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-11-16T10:31:07",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-16T10:31:07",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-16T10:37:55",
						"datotype": "DATO_JOURNALFOERT"
					}, {
						"dato": "2023-11-16T10:31:07",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "NAV 90-00.08 K Klage",
						"dokumentInfoId": "454243177",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["skannet_dokument"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "vedlegg: 1",
						"dokumentInfoId": "454243178",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["skannet_dokument"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "NAV 90-00.08 K Klage",
					"journalpostId": "453851775",
					"journalposttype": "I",
					"journalstatus": "JOURNALFOERT",
					"avsender": null,
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-11-16T10:31:04",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-16T10:31:04",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-16T10:36:28",
						"datotype": "DATO_JOURNALFOERT"
					}, {
						"dato": "2023-11-16T10:31:04",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "NAV 90-00.08 K Klage",
						"dokumentInfoId": "454243175",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["ingen_partsinnsyn", "skannet_dokument"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "vedlegg: 1",
						"dokumentInfoId": "454243176",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["ingen_partsinnsyn", "skannet_dokument"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "NAV 90-00.08 K Klage",
					"journalpostId": "453851745",
					"journalposttype": "I",
					"journalstatus": "JOURNALFOERT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-11-16T09:36:59",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-16T09:36:59",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-16T10:31:32",
						"datotype": "DATO_JOURNALFOERT"
					}, {
						"dato": "2023-11-16T09:36:59",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "NAV 90-00.08 K Klage",
						"dokumentInfoId": "454243143",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["skannet_dokument"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "vedlegg: 1",
						"dokumentInfoId": "454243144",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["skannet_dokument"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "NAV 15-00.02 Søknad om stønad til barnetilsyn - enslig mor eller far i arbeid",
					"journalpostId": "453851744",
					"journalposttype": "I",
					"journalstatus": "JOURNALFOERT",
					"avsender": null,
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-11-16T09:36:51",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-16T09:36:51",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-16T10:33:19",
						"datotype": "DATO_JOURNALFOERT"
					}, {
						"dato": "2023-11-16T09:36:51",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "NAV 15-00.02 Søknad om stønad til barnetilsyn - enslig mor eller far i arbeid",
						"dokumentInfoId": "454243141",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["ingen_partsinnsyn", "skannet_dokument"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "Bekreftelse fra barnevernet",
						"dokumentInfoId": "454243142",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": false,
							"code": ["ingen_partsinnsyn", "skannet_dokument"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "NAV 15-00.01 Søknad om overgangsstønad - enslig mor eller far",
					"journalpostId": "453851743",
					"journalposttype": "I",
					"journalstatus": "JOURNALFOERT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-11-16T09:36:41",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-16T09:36:41",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-16T10:34:47",
						"datotype": "DATO_JOURNALFOERT"
					}, {
						"dato": "2023-11-16T09:36:41",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "NAV 15-00.01 Søknad om overgangsstønad - enslig mor eller far",
						"dokumentInfoId": "454243139",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "vedlegg: 1",
						"dokumentInfoId": "454243140",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}]
				}, {
					"tittel": "NAV 90-00.08 K Klage",
					"journalpostId": "453851742",
					"journalposttype": "I",
					"journalstatus": "JOURNALFOERT",
					"avsender": {
						"id": "08909298858",
						"type": "FNR"
					},
					"mottaker": null,
					"relevanteDatoer": [{
						"dato": "2023-11-16T09:36:35",
						"datotype": "DATO_OPPRETTET"
					}, {
						"dato": "2023-11-16T09:36:35",
						"datotype": "DATO_DOKUMENT"
					}, {
						"dato": "2023-11-16T09:43:42",
						"datotype": "DATO_JOURNALFOERT"
					}, {
						"dato": "2023-11-16T09:36:35",
						"datotype": "DATO_REGISTRERT"
					}],
					"dokumenter": [{
						"tittel": "NAV 90-00.08 K Klage",
						"dokumentInfoId": "454243137",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}, {
						"tittel": "Klagevedlegg",
						"dokumentInfoId": "454243138",
						"dokumentvarianter": [{
							"variantformat": "ARKIV",
							"brukerHarTilgang": true,
							"code": ["ok"],
							"filtype": "PDF"
						}]
					}]
				}]
			}]
	
	}
}"""
}
