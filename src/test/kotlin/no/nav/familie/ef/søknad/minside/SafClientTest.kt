package no.nav.familie.ef.søknad.minside

import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.familie.ef.søknad.minside.domain.DokumentoversiktSelvbetjeningResponse
import no.nav.familie.ef.søknad.minside.dto.SafDokumentOversiktResponse
import no.nav.familie.kontrakter.felles.objectMapper
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
        objectMapper.readValue<SafDokumentOversiktResponse<DokumentoversiktSelvbetjeningResponse>>(response)
    }
}
