package no.nav.familie.ef.søknad.api

import no.nav.familie.ef.søknad.integration.SøknadClient
import no.nav.familie.kontrakter.ef.ettersending.SøknadMedDokumentasjonsbehovDto
import no.nav.familie.sikkerhet.EksternBrukerUtils
import no.nav.security.token.support.core.api.ProtectedWithClaims
import no.nav.security.token.support.core.api.RequiredIssuers
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api/dokumentasjonsbehov"])
@RequiredIssuers(
    ProtectedWithClaims(issuer = "tokenx", claimMap = ["acr=Level4"]),
    ProtectedWithClaims(issuer = "selvbetjening", claimMap = ["acr=Level4"])
)
class DokumentasjonsbehovController(private val søknadClient: SøknadClient) {

    @GetMapping("/person")
    fun hentDokumentasjonsbehovForPerson(): ResponseEntity<List<SøknadMedDokumentasjonsbehovDto>> {
        val ident = EksternBrukerUtils.hentFnrFraToken()
        return ResponseEntity.ok(søknadClient.hentSøknaderMedDokumentasjonsbehov(ident))
    }
}
