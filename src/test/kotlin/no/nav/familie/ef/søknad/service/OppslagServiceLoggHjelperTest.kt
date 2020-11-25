package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.api.dto.Søkerinfo
import no.nav.familie.ef.søknad.api.dto.tps.Adresse
import no.nav.familie.ef.søknad.api.dto.tps.Barn
import no.nav.familie.kontrakter.felles.objectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class OppslagServiceLoggHjelperTest {

    @Test
    fun `Skal ikke logge forskjeller når alt er likt`() {
        val søkerinfo = objectMapper.readValue(søkerInfo, Søkerinfo::class.java)

        val søkerPdl = søkerinfo.søker.copy(sivilstand = "UGIFT")
        val søkerTps = søkerinfo.søker.copy(sivilstand = "UGIF")

        val barnPdl = søkerinfo.barn.first().copy(navn = "Ola Olsen")
        val barnTps = søkerinfo.barn.first().copy(navn = "Olsen Ola")

        val søkerinfoPdl = søkerinfo.copy(søker = søkerPdl, barn = listOf(barnPdl))
        val søkerinfoTps = søkerinfo.copy(søker = søkerTps, barn = listOf(barnTps))
        assertThat(OppslagServiceLoggHjelper.logDiff(søkerinfoTps, søkerinfoPdl)).isBlank
    }

    @Test
    fun `Skal logge forskjeller på Person og barn data`() {
        val søkerinfo = objectMapper.readValue(søkerInfo, Søkerinfo::class.java)
        val endretSøker = søkerinfo.søker.copy(fnr = "54325432",
                                               adresse = Adresse("nyAdresse", "7654", "AnnetSted"),
                                               statsborgerskap = "ANNET",
                                               sivilstand = "UKJENT",
                                               egenansatt = true,
                                               forkortetNavn = "KORT")
        val barn = Barn("456776544567",
                        "Ole Annetnavn",
                        2,
                        LocalDate.now(),
                        false)
        val søkerinfo2 = søkerinfo.copy(søker = endretSøker, barn = listOf(barn))
        assertThat(OppslagServiceLoggHjelper.logDiff(søkerinfo, søkerinfo2)).isNotBlank
    }

    @Test
    fun `Skal logge forskjeller - manglende barn`() {
        val søkerinfo = objectMapper.readValue(søkerInfo, Søkerinfo::class.java)
        val søkerinfo2 = søkerinfo.copy(barn = listOf())
        assertThat(OppslagServiceLoggHjelper.logDiff(søkerinfo, søkerinfo2)).isNotBlank
    }
}


val søkerInfo = """{
  "søker": {
    "fnr": "08018820243",
    "forkortetNavn": "TUNGSINDIG GASELLE",
    "adresse": {
      "adresse": "SELSBAKKEN 93",
      "postnummer": "0561",
      "poststed": "OSLO"
    },
    "egenansatt": false,
    "sivilstand": "UGIF",
    "statsborgerskap": "NORGE"
  },
  "barn": [
    {
      "fnr": "28021961053",
      "navn": "DORULL RASK",
      "alder": 1,
      "fødselsdato": "2019-02-28",
      "harSammeAdresse": true
    }
  ],
  "hash": "664551484"
} """