package no.nav.familie.ef.søknad.service

import no.nav.familie.ef.søknad.integration.FamilieIntegrasjonerClient
import no.nav.familie.kontrakter.felles.kodeverk.KodeverkDto
import no.nav.familie.kontrakter.felles.kodeverk.hentGjelende
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class KodeverkService(private val integrasjonerClient: FamilieIntegrasjonerClient) {

    fun hentLand(landkode: String): String? {
        return hentLandkoder().hentGjelende(landkode, LocalDate.now())
    }

    fun hentPoststed(postnummer: String): String? {
        return hentPoststed().hentGjelende(postnummer, LocalDate.now())
    }

    @Cacheable("kodeverk_landkoder")
    fun hentLandkoder(): KodeverkDto {
        return integrasjonerClient.hentKodeverkLandkoder()
    }

    @Cacheable("kodeverk_poststed")
    fun hentPoststed(): KodeverkDto {
        return integrasjonerClient.hentKodeverkPoststed()
    }

}
