package no.nav.familie.ef.søknad.mock

import no.nav.familie.ef.søknad.integration.TpsInnsynServiceClient
import no.nav.familie.ef.søknad.integration.dto.*
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import java.time.LocalDate

@Configuration
class InnsynTestConfiguration {

// eksempeldata fra TPS innsyn swagger
//    {
//        "ident": "28129124085",
//        "identtype": {
//        "verdi": "FNR",
//        "kodeverk": "Personidenter"
//    },
//        "kjonn": "K",
//        "alder": 27,
//        "foedselsdato": "1991-12-28",
//        "foedtILand": null,
//        "foedtIKommune": null,
//        "datoFraOgMed": "2019-10-08",
//        "kilde": "SKD",
//        "status": {
//        "datoFraOgMed": "1991-12-28",
//        "kilde": "SKD",
//        "kode": {
//        "verdi": "BOSA",
//        "kodeverk": "Personstatuser"
//    }
//    },
//        "navn": {
//        "datoFraOgMed": "2019-10-08",
//        "kilde": "SKD",
//        "forkortetNavn": "KOPP LUNKEN",
//        "slektsnavn": "KOPP",
//        "fornavn": "LUNKEN",
//        "mellomnavn": "",
//        "slektsnavnUgift": ""
//    },
//        "spraak": {
//        "datoFraOgMed": "1991-12-28",
//        "kilde": "TPSF",
//        "kode": {
//        "verdi": "NB",
//        "kodeverk": "Språk"
//    }
//    },
//        "sivilstand": {
//        "datoFraOgMed": "",
//        "kilde": "SKD",
//        "kode": {
//        "verdi": "UGIF",
//        "kodeverk": "Sivilstander"
//    }
//    },
//        "statsborgerskap": {
//        "datoFraOgMed": "1991-12-28",
//        "kilde": "SKD",
//        "kode": {
//        "verdi": "NOR",
//        "kodeverk": "StatsborgerskapFreg"
//    }
//    },
//        "doedsdato": null,
//        "spesiellOpplysning": null,
//        "tiltak": null,
//        "egenansatt": {
//        "datoFraOgMed": null,
//        "kilde": null,
//        "erEgenansatt": false
//    },
//        "telefon": null,
//        "adresseinfo": {
//        "boadresse": {
//        "datoFraOgMed": "1991-12-28",
//        "kilde": "SKD",
//        "adresse": "JAABÆKVEGEN 16",
//        "landkode": "NOR",
//        "kommune": "0104",
//        "postnummer": "1533",
//        "bydel": null,
//        "adressetillegg": null,
//        "veiadresse": {
//        "gatekode": "06160",
//        "husnummer": "16",
//        "bokstav": null,
//        "bolignummer": null
//    },
//        "matrikkeladresse": {
//        "gaardsnummer": null,
//        "bruksnummer": null,
//        "festenummer": null,
//        "undernummer": null
//    }
//    },
//        "postadresse": null,
//        "prioritertAdresse": null,
//        "geografiskTilknytning": {
//        "datoFraOgMed": "1991-12-28",
//        "kilde": "SKD",
//        "land": null,
//        "kommune": "0104",
//        "bydel": null
//    },
//        "tilleggsadresse": null,
//        "utenlandskAdresse": null
//    },
//        "antallBarn": 1,
//        "antallLevendeBarnUnder18": 1,
//        "relasjonFinnes": false,
//        "foreldreansvar": null,
//        "oppholdstillatelse": null,
//        "kontonummer": null,
//        "innvandringUtvandring": {
//        "innvandretLand": {
//        "verdi": "BWA",
//        "kodeverk": "Landkoder"
//    },
//        "innvandretDato": "1991-12-28",
//        "innvandretKilde": "SKD",
//        "utvandretLand": {
//        "verdi": "",
//        "kodeverk": "Landkoder"
//    },
//        "utvandretDato": null,
//        "utvandretKilde": null
//    },
//        "vergemaalListe": [],
//        "brukerbehovListe": [],
//        "utenlandsinfoList": [],
//        "utenlandskBank": null
//    }

    @Bean
    @Profile("mock-tps")
    @Primary
    internal fun innsynServiceGen(): TpsInnsynServiceClient {

        val innsynServiceClient = mock(TpsInnsynServiceClient::class.java)
        val personinfoDto = PersoninfoDto(ident = "12345678911",
                                          navn = NavnDto("KOPP LUNKEN"),
                                          adresseinfo = AdresseinfoDto(BostedsadresseDto("JAABÆKVEGEN 16",
                                                                                         "v/noen",
                                                                                         "0104",
                                                                                         "NOR",
                                                                                         "1533")),
                                          dødsdato = DødsdatoDto(null),
                                          egenansatt = EgenansattDto(LocalDate.of(2017, 8, 30), true),
                                          foreldreansvar = KodeMedDatoOgKildeDto(KodeDto("foreldreansvar kode")),
                                          innvandringUtvandring = InnvandringUtvandringDto(LocalDate.of(1991, 5, 4), null),
                                          kontonummer = KontonummerDto("8675309"),
                                          oppholdstillatelse = KodeMedDatoOgKildeDto(KodeDto("opphold")),
                                          sivilstand = KodeMedDatoOgKildeDto(KodeDto("UGIFT")),
                                          språk = KodeMedDatoOgKildeDto(KodeDto("NB")),
                                          statsborgerskap = KodeMedDatoOgKildeDto(KodeDto("NOR")),
                                          telefon = TelefoninfoDto("jobb", "mobil", "privat")
        )

        val relasjonDto = RelasjonDto(ident="fødselsnummer",
                                      forkortetNavn = "Bob Kåre",
                                      alder = 15,
                                      dødsdato = null,
                                      fødselsdato = LocalDate.of(2004, 4, 15),
                                      harSammeAdresse = true)

        `when`(innsynServiceClient.hentPersoninfo()).thenReturn(personinfoDto)
        `when`(innsynServiceClient.hentBarn()).thenReturn(listOf(relasjonDto))
        return innsynServiceClient
    }


}
