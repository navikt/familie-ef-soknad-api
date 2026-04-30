package no.nav.familie.ef.søknad.kodeverk

import no.nav.familie.kontrakter.felles.kodeverk.KodeverkDto
import no.nav.familie.kontrakter.felles.kodeverk.hentGjeldende
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
open class KodeverkService(
    private val cachedKodeverkService: CachedKodeverkService,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Service
    class CachedKodeverkService(
        private val integrasjonerClient: FamilieIntegrasjonerClient,
    ) {
        @Cacheable("kodeverk_landkoder")
        fun hentLandkoder(): KodeverkDto = integrasjonerClient.hentKodeverkLandkoder()

        @Cacheable("kodeverk_poststed")
        fun hentPoststed(): KodeverkDto = integrasjonerClient.hentKodeverkPoststed()
    }

    fun hentLand(landkode: String): String? = cachedKodeverkService.hentLandkoder().hentGjeldende(landkode, LocalDate.now())

    fun hentPoststed(postnummer: String): String? = cachedKodeverkService.hentPoststed().hentGjeldende(postnummer, LocalDate.now())

    fun hentLandkoder(spraak: Spraak): List<Landkode> =
        try {
            mapTilLandkoder(kodeverkDto = cachedKodeverkService.hentLandkoder(), spraak = spraak)
        } catch (e: Exception) {
            logger.warn("Kunne ikke hente landkoder fra kodeverk, bruker fallback-liste", e)
            fallbackLandliste(spraak)
        }

    private fun mapTilLandkoder(
        kodeverkDto: KodeverkDto,
        spraak: Spraak,
    ): List<Landkode> {
        val idag = LocalDate.now()
        return kodeverkDto.betydninger
            .mapNotNull { (alpha3, betydninger) ->
                if (alpha3.isBlank()) return@mapNotNull null

                val gjeldende =
                    betydninger.firstOrNull { betydning ->
                        !betydning.gyldigFra.isAfter(idag) && !betydning.gyldigTil.isBefore(idag)
                    } ?: return@mapNotNull null

                val navnFraKodeverk = gjeldende.beskrivelser["nb"]?.tekst?.takeIf { it.isNotBlank() }
                val navn =
                    lokalisertLandnavn(alpha3 = alpha3, spraak = spraak)
                        ?: navnFraKodeverk?.let { tilTitlecase(input = it, locale = spraak.locale) }
                        ?: return@mapNotNull null

                Landkode(kode = alpha3, navn = navn, erEøsland = alpha3 in EOS_LAND)
            }.sortedWith(compareBy(spraak.collator) { it.navn })
    }
}
