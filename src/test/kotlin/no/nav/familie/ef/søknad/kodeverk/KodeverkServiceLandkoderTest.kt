package no.nav.familie.ef.søknad.kodeverk

import io.mockk.every
import io.mockk.mockk
import no.nav.familie.kontrakter.felles.kodeverk.BeskrivelseDto
import no.nav.familie.kontrakter.felles.kodeverk.BetydningDto
import no.nav.familie.kontrakter.felles.kodeverk.KodeverkDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.web.client.RestClientException
import java.time.LocalDate

internal class KodeverkServiceLandkoderTest {
    private val integrasjonerClient: FamilieIntegrasjonerClient = mockk()
    private val cachedKodeverkService = KodeverkService.CachedKodeverkService(integrasjonerClient)
    private val kodeverkService = KodeverkService(cachedKodeverkService = cachedKodeverkService)

    @Test
    fun `mapper alpha3 koder og bevarer norske navn fra kodeverk`() {
        every { integrasjonerClient.hentKodeverkLandkoder() } returns
            kodeverk(
                "NOR" to "Norge",
                "SWE" to "Sverige",
                "BRA" to "Brasil",
            )

        val land = kodeverkService.hentLandkoder(Spraak.NB)

        assertThat(land.map { it.kode }).containsExactlyInAnyOrder("NOR", "SWE", "BRA")
        assertThat(land.first { it.kode == "SWE" }.navn).isEqualTo("Sverige")
    }

    @Test
    fun `markerer EØS land riktig`() {
        every { integrasjonerClient.hentKodeverkLandkoder() } returns
            kodeverk(
                "SWE" to "Sverige",
                "DEU" to "Tyskland",
                "BRA" to "Brasil",
                "USA" to "USA",
                "ISL" to "Island",
            )

        val land = kodeverkService.hentLandkoder(Spraak.NB).associateBy { it.kode }

        assertThat(land["SWE"]?.erEøsland).isTrue
        assertThat(land["DEU"]?.erEøsland).isTrue
        assertThat(land["ISL"]?.erEøsland).isTrue
        assertThat(land["BRA"]?.erEøsland).isFalse
        assertThat(land["USA"]?.erEøsland).isFalse
    }

    @Test
    fun `filtrerer bort utgåtte koder`() {
        val idag = LocalDate.now()
        val gyldigKodeverk =
            KodeverkDto(
                betydninger =
                    mapOf(
                        "NOR" to listOf(betydning(verdi = "Norge", gyldigFra = idag.minusYears(10), gyldigTil = idag.plusYears(10))),
                        "SUN" to listOf(betydning(verdi = "Sovjetunionen", gyldigFra = idag.minusYears(80), gyldigTil = idag.minusYears(30))),
                    ),
            )
        every { integrasjonerClient.hentKodeverkLandkoder() } returns gyldigKodeverk

        val land = kodeverkService.hentLandkoder(Spraak.NB)

        assertThat(land.map { it.kode }).containsExactly("NOR")
    }

    @Test
    fun `sorterer alfabetisk etter visningsspråk`() {
        every { integrasjonerClient.hentKodeverkLandkoder() } returns
            kodeverk(
                "ZWE" to "Zimbabwe",
                "AUT" to "Østerrike",
                "DNK" to "Danmark",
            )

        val land = kodeverkService.hentLandkoder(Spraak.NB)

        assertThat(land.map { it.kode }).containsExactly("DNK", "ZWE", "AUT")
    }

    @Test
    fun `bruker JDK lokalisering for nn og en når kodeverk kun har bokmål`() {
        every { integrasjonerClient.hentKodeverkLandkoder() } returns
            kodeverk("DEU" to "Tyskland")

        val nb = kodeverkService.hentLandkoder(Spraak.NB).first()
        val en = kodeverkService.hentLandkoder(Spraak.EN).first()

        assertThat(nb.navn).isEqualTo("Tyskland")
        assertThat(en.navn).isEqualTo("Germany")
    }

    @Test
    fun `fallback til statisk liste når kodeverk returnerer tom liste`() {
        every { integrasjonerClient.hentKodeverkLandkoder() } returns KodeverkDto(betydninger = emptyMap())

        val land = kodeverkService.hentLandkoder(Spraak.NB)

        assertThat(land).isNotEmpty
        assertThat(land.map { it.kode }).contains("NOR", "SWE", "DEU")
    }

    @Test
    fun `fallback til statisk liste når kodeverk feiler`() {
        every { integrasjonerClient.hentKodeverkLandkoder() } throws RestClientException("Kodeverk nede")

        val land = kodeverkService.hentLandkoder(Spraak.NB)

        assertThat(land).isNotEmpty
        assertThat(land.map { it.kode }).contains("NOR", "SWE", "DEU", "BRA")
        assertThat(land.first { it.kode == "DEU" }.erEøsland).isTrue
        assertThat(land.first { it.kode == "BRA" }.erEøsland).isFalse
    }

    @Test
    fun `returnerer titlecase navn istedenfor kodeverks i caps`() {
        every { integrasjonerClient.hentKodeverkLandkoder() } returns
            kodeverk("AFG" to "AFGHANISTAN")

        val land = kodeverkService.hentLandkoder(Spraak.NB)

        assertThat(land.first { it.kode == "AFG" }.navn).isEqualTo("Afghanistan")
    }

    @Test
    fun `titlecaser kodeverk-tekst når JDK ikke kjenner alpha3`() {
        every { integrasjonerClient.hentKodeverkLandkoder() } returns
            kodeverk("XYZ" to "STOR-BRITANNIA OG NORD-IRLAND")

        val land = kodeverkService.hentLandkoder(Spraak.NB)

        assertThat(land.map { it.navn }).containsExactly("Stor-Britannia Og Nord-Irland")
    }

    @Test
    fun `dropper kodeverk-entries med tom tekst eller blank alpha3`() {
        every { integrasjonerClient.hentKodeverkLandkoder() } returns
            KodeverkDto(
                betydninger =
                    mapOf(
                        "NOR" to listOf(BetydningDto(LocalDate.MIN, LocalDate.MAX, mapOf("nb" to BeskrivelseDto("Norge", "Norge")))),
                        "" to listOf(BetydningDto(LocalDate.MIN, LocalDate.MAX, mapOf("nb" to BeskrivelseDto("Skal droppes", "Skal droppes")))),
                        "QQQ" to listOf(BetydningDto(LocalDate.MIN, LocalDate.MAX, mapOf("nb" to BeskrivelseDto("", "")))),
                    ),
            )

        val land = kodeverkService.hentLandkoder(Spraak.NB)

        assertThat(land.map { it.kode }).containsExactly("NOR")
    }

    @Test
    fun `dropper koder uten gyldig norsk beskrivelse`() {
        every { integrasjonerClient.hentKodeverkLandkoder() } returns
            KodeverkDto(
                betydninger =
                    mapOf(
                        "NOR" to listOf(BetydningDto(LocalDate.MIN, LocalDate.MAX, mapOf("nb" to BeskrivelseDto("Norge", "Norge")))),
                        "XXX" to listOf(BetydningDto(LocalDate.MIN, LocalDate.MAX, mapOf("se" to BeskrivelseDto("Foo", "Foo")))),
                    ),
            )

        val land = kodeverkService.hentLandkoder(Spraak.NB)

        assertThat(land.map { it.kode }).containsExactly("NOR")
    }

    private fun kodeverk(vararg kodeOgVerdi: Pair<String, String>): KodeverkDto =
        KodeverkDto(
            betydninger =
                kodeOgVerdi.associate { (kode, verdi) ->
                    kode to listOf(betydning(verdi = verdi))
                },
        )

    private fun betydning(
        verdi: String,
        gyldigFra: LocalDate = LocalDate.MIN,
        gyldigTil: LocalDate = LocalDate.MAX,
    ): BetydningDto =
        BetydningDto(
            gyldigFra = gyldigFra,
            gyldigTil = gyldigTil,
            beskrivelser = mapOf("nb" to BeskrivelseDto(term = verdi, tekst = verdi)),
        )
}
