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

    fun hentLandkoder(språk: Språk): List<Landkode> =
        try {
            val resultat = mapTilLandkoder(kodeverkDto = cachedKodeverkService.hentLandkoder(), språk = språk)

            if (resultat.isEmpty()) {
                logger.error("Tom landkodeliste fra kodeverk for språk: ${språk.tag} – bruker fallbackliste")
                fallbackLandliste(språk)
            } else {
                resultat
            }
        } catch (e: Exception) {
            logger.warn("Kunne ikke hente landkoder fra kodeverk, bruker fallbackliste", e)
            fallbackLandliste(språk)
        }

    private fun mapTilLandkoder(
        kodeverkDto: KodeverkDto,
        språk: Språk,
    ): List<Landkode> {
        val idag = LocalDate.now()
        return kodeverkDto.betydninger
            .mapNotNull { (alpha3, betydningerForKode) ->
                if (alpha3.isBlank()) return@mapNotNull null

                val gjeldendeBetydning =
                    betydningerForKode.firstOrNull { betydning ->
                        !betydning.gyldigFra.isAfter(idag) && !betydning.gyldigTil.isBefore(idag)
                    } ?: return@mapNotNull null

                val navnFraKodeverk = gjeldendeBetydning.beskrivelser["nb"]?.tekst?.takeIf { it.isNotBlank() }
                val navn =
                    lokalisertLandnavn(alpha3 = alpha3, språk = språk)
                        ?: navnFraKodeverk?.let { tilTitlecase(input = it, locale = språk.locale) }
                        ?: return@mapNotNull null

                Landkode(kode = alpha3, navn = navn, erEøsland = alpha3 in EOS_LAND)
            }.sortedWith(compareBy(språk.collator()) { it.navn })
    }
}
