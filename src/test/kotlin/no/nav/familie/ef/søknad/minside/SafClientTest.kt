package no.nav.familie.ef.søknad.minside

import no.nav.familie.ef.søknad.infrastruktur.config.readValue
import no.nav.familie.ef.søknad.minside.domain.DokumentoversiktSelvbetjeningResponse
import no.nav.familie.ef.søknad.minside.dto.SafDokumentOversiktResponse
import no.nav.familie.kontrakter.felles.jsonMapper
import org.junit.jupiter.api.Test

class SafClientTest {
    @Test
    fun mapping() {
        val response =
            """
            {
              "data": {
                "dokumentoversiktSelvbetjening": {
                  "tema": [
                    {
                      "kode": "ENF",
                      "navn": "Enslig forsørger",
                      "journalposter": []
                    }
                  ]
                }
              }
            }
            """.trimIndent()
        jsonMapper.readValue<SafDokumentOversiktResponse<DokumentoversiktSelvbetjeningResponse>>(response)
    }
}
