package no.nav.familie.ef.søknad.api

import no.nav.familie.ef.søknad.integration.SøknadClient
import no.nav.familie.ef.søknad.util.InnloggingUtils
import no.nav.familie.ef.søknad.util.InnloggingUtils.sjekkPersonIdentMotInnloggetBruker
import no.nav.familie.kontrakter.ef.søknad.dokumentasjonsbehov.DokumentasjonsbehovDto
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(path = ["/api/dokumentasjonsbehov"])
@ProtectedWithClaims(issuer = InnloggingUtils.ISSUER, claimMap = ["acr=Level4"])
class DokumentasjonsbehovController(private val søknadClient: SøknadClient) {

    @GetMapping("/{soknadId}")
    fun hentDokumentasjonsbehov(@PathVariable("soknadId") søknadId: UUID): ResponseEntity<DokumentasjonsbehovDto> {
        val dokumentasjonsbehovDto = søknadClient.hentDokumentasjonsbehovForSøknad(søknadId)
        sjekkPersonIdentMotInnloggetBruker(dokumentasjonsbehovDto.personIdent)

        return ResponseEntity.ok(dokumentasjonsbehovDto)
    }
}