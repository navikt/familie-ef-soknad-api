package no.nav.familie.ef.søknad.mock

import no.nav.security.token.support.core.api.Unprotected
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/mockapi/"], produces = [MediaType.APPLICATION_JSON_VALUE])
@Unprotected
class TpsInnsynMockController {


    @GetMapping("/barn")
    fun barnFraTpsMocked(): String {
        return barn
    }

    @GetMapping("/person")
    fun søkerinfoFraTpsMocked(): String {
        return person
    }

    // language=jSon
    val barn: String = """ 
[
  {
    "datoFraOgMed": null,
    "kilde": null,
    "ident": "28021078036",
    "relasjonsType": {
      "verdi": "BARN",
      "kodeverk": "Familierelasjoner"
    },
    "forkortetNavn": "KARAFFEL STERK",
    "kjoenn": "K",
    "foedselsdato": "2010-02-28",
    "alder": 9,
    "harSammeAdresse": true,
    "statsborgerskap": {
      "datoFraOgMed": "2019-10-08",
      "kilde": "SKD",
      "kode": {
        "verdi": "NOR",
        "kodeverk": "StatsborgerskapFreg"
      }
    },
    "doedsdato": null,
    "spesiellOpplysning": null,
    "egenansatt": {
      "datoFraOgMed": null,
      "kilde": null,
      "erEgenansatt": false
    }
  }
]
"""
    // language=jSon
    val person: String = """
        {
  "ident": "21057822284",
  "identtype": {
    "verdi": "FNR",
    "kodeverk": "Personidenter"
  },
  "kjonn": "K",
  "alder": 27,
  "foedselsdato": "1991-12-28",
  "foedtILand": null,
  "foedtIKommune": null,
  "datoFraOgMed": "2019-10-08",
  "kilde": "SKD",
  "status": {
    "datoFraOgMed": "1991-12-28",
    "kilde": "SKD",
    "kode": {
      "verdi": "BOSA",
      "kodeverk": "Personstatuser"
    }
  },
  "navn": {
    "datoFraOgMed": "2019-10-08",
    "kilde": "SKD",
    "forkortetNavn": "KOPP LUNKEN",
    "slektsnavn": "KOPP",
    "fornavn": "LUNKEN",
    "mellomnavn": "",
    "slektsnavnUgift": ""
  },
  "spraak": {
    "datoFraOgMed": "1991-12-28",
    "kilde": "TPSF",
    "kode": {
      "verdi": "NB",
      "kodeverk": "Språk"
    }
  },
  "sivilstand": {
    "datoFraOgMed": "",
    "kilde": "SKD",
    "kode": {
      "verdi": "UGIF",
      "kodeverk": "Sivilstander"
    }
  },
  "statsborgerskap": {
    "datoFraOgMed": "1991-12-28",
    "kilde": "SKD",
    "kode": {
      "verdi": "NOR",
      "kodeverk": "StatsborgerskapFreg"
    }
  },
  "doedsdato": null,
  "spesiellOpplysning": null,
  "tiltak": null,
  "egenansatt": {
    "datoFraOgMed": null,
    "kilde": null,
    "erEgenansatt": false
  },
  "telefon": null,
  "adresseinfo": {
    "boadresse": {
      "datoFraOgMed": "1991-12-28",
      "kilde": "SKD",
      "adresse": "JAABÆKVEGEN 16",
      "landkode": "NOR",
      "kommune": "0104",
      "postnummer": "1533",
      "bydel": null,
      "adressetillegg": null,
      "veiadresse": {
        "gatekode": "06160",
        "husnummer": "16",
        "bokstav": null,
        "bolignummer": null
      },
      "matrikkeladresse": {
        "gaardsnummer": null,
        "bruksnummer": null,
        "festenummer": null,
        "undernummer": null
      }
    },
    "postadresse": null,
    "prioritertAdresse": null,
    "geografiskTilknytning": {
      "datoFraOgMed": "1991-12-28",
      "kilde": "SKD",
      "land": null,
      "kommune": "0104",
      "bydel": null
    },
    "tilleggsadresse": null,
    "utenlandskAdresse": null
  },
  "antallBarn": 1,
  "antallLevendeBarnUnder18": 1,
  "relasjonFinnes": false,
  "foreldreansvar": null,
  "oppholdstillatelse": null,
  "kontonummer": null,
  "innvandringUtvandring": {
    "innvandretLand": {
      "verdi": "BWA",
      "kodeverk": "Landkoder"
    },
    "innvandretDato": "1991-12-28",
    "innvandretKilde": "SKD",
    "utvandretLand": {
      "verdi": "",
      "kodeverk": "Landkoder"
    },
    "utvandretDato": null,
    "utvandretKilde": null
  },
  "vergemaalListe": [],
  "brukerbehovListe": [],
  "utenlandsinfoList": [],
  "utenlandskBank": null
}
    """

}