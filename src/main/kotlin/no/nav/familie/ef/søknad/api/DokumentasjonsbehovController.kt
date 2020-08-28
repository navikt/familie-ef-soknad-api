package no.nav.familie.ef.søknad.api

import no.nav.familie.ef.søknad.integration.SøknadClient
import no.nav.familie.kontrakter.ef.søknad.dokumentasjonsbehov.DokumentasjonsbehovDto
import no.nav.familie.sikkerhet.EksternBrukerUtils
import no.nav.familie.sikkerhet.EksternBrukerUtils.personIdentErLikInnloggetBruker
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(path = ["/api/dokumentasjonsbehov"])
@ProtectedWithClaims(issuer = EksternBrukerUtils.ISSUER, claimMap = ["acr=Level4"])
class DokumentasjonsbehovController(private val søknadClient: SøknadClient) {

    @GetMapping("/{soknadId}")
    fun hentDokumentasjonsbehov(@PathVariable("soknadId") søknadId: UUID): ResponseEntity<DokumentasjonsbehovDto> {
        val dokumentasjonsbehovDto = søknadClient.hentDokumentasjonsbehovForSøknad(søknadId)

        if (personIdentErLikInnloggetBruker(dokumentasjonsbehovDto.personIdent)) {
            throw ApiFeil("Fnr fra token matcher ikke fnr på søknaden", HttpStatus.FORBIDDEN)
        }

        return ResponseEntity.ok(dokumentasjonsbehovDto)
    }
}