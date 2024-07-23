package no.nav.familie.ef.søknad.kodeverk

import no.nav.familie.kontrakter.felles.kodeverk.KodeverkDto
import no.nav.familie.kontrakter.felles.kodeverk.hentGjeldende
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class KodeverkService(
    private val cachedKodeverkService: CachedKodeverkService,
) {
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
}
