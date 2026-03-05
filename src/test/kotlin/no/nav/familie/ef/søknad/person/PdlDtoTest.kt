package no.nav.familie.ef.søknad.person

import no.nav.familie.ef.søknad.infrastruktur.config.readValue
import no.nav.familie.ef.søknad.person.dto.PdlResponse
import no.nav.familie.ef.søknad.person.dto.PdlSøkerData
import no.nav.familie.kontrakter.felles.jsonMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import tools.jackson.databind.SerializationFeature
import tools.jackson.databind.json.JsonMapper

class PdlDtoTest {
    @Test
    fun `pdlSøkerData inneholder samme felter som blir spurt om i query`() {
        val spørringsfelter = PdlTestUtil.parseSpørring("/pdl/søker.graphql")

        val dtoFelter = PdlTestUtil.finnFeltStruktur(PdlTestdata.pdlSøkerData)!!

        assertThat(dtoFelter).isEqualTo(spørringsfelter["data"])
    }

    @Test
    fun `pdlBarnData inneholder samme felter som blir spurt om i query`() {
        val spørringsfelter = PdlTestUtil.parseSpørring("/pdl/barn.graphql")

        val dtoFelter = PdlTestUtil.finnFeltStruktur(PdlTestdata.pdlBarnData)!!

        val writerWithDefaultPrettyPrinter: JsonMapper = jsonMapper.rebuild().enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS).build()

        assertThat(writerWithDefaultPrettyPrinter.writeValueAsString(dtoFelter))
            .isEqualTo(writerWithDefaultPrettyPrinter.writeValueAsString(spørringsfelter["data"]))
    }

    @Test
    fun `Skal kunne mappe resultat fra pdl`() {
        val returFraPdlSøker: String =
            """
            {
              "data": {
                "person": {
                  "adressebeskyttelse": [],
                  "foedselsdato": [
                    {
                      "foedselsdato": "1993-08-18",
                      "foedselsaar": 1993
                    }
                  ],
                  "bostedsadresse": [
                    {
                      "vegadresse": {
                        "adressenavn": "Borggata",
                        "bruksenhetsnummer": "H0101",
                        "husbokstav": null,
                        "husnummer": "78",
                        "matrikkelId": 191692212,
                        "postnummer": "5417"
                      },
                      "matrikkeladresse": null
                    }
                  ],
                  "forelderBarnRelasjon": [
                    {
                      "relatertPersonsIdent": "30906398490",
                      "relatertPersonsRolle": "MOR"
                    },
                    {
                      "relatertPersonsIdent": "29816197315",
                      "relatertPersonsRolle": "FAR"
                    },
                    {
                      "relatertPersonsIdent": "19812288930",
                      "relatertPersonsRolle": "BARN"
                    }
                  ],
                  "navn": [
                    {
                      "fornavn": "FYLDIG",
                      "mellomnavn": null,
                      "etternavn": "JORDSKORPE"
                    }
                  ],
                  "sivilstand": [
                    {
                      "type": "UGIFT"
                    }
                  ],
                  "statsborgerskap": [
                    {
                      "land": "NOR",
                      "gyldigFraOgMed": "1993-08-18",
                      "gyldigTilOgMed": null
                    }
                  ]
                }
              }
            }
            """.trimIndent()

        val readValue = jsonMapper.readValue<PdlResponse<PdlSøkerData>>(returFraPdlSøker)

        assertThat(
            readValue.data.person!!
                .navn
                .first()
                .fornavn,
        ).isEqualTo("FYLDIG")
    }
}
