package no.nav.familie.ef.søknad.ettersending

import no.nav.familie.ef.søknad.søknad.MottakClient
import no.nav.familie.kontrakter.ef.ettersending.SøknadMedDokumentasjonsbehovDto
import no.nav.familie.sikkerhet.EksternBrukerUtils
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api/dokumentasjonsbehov"])
@ProtectedWithClaims(issuer = EksternBrukerUtils.ISSUER_TOKENX, claimMap = ["acr=Level4"])
class DokumentasjonsbehovController(
    private val søknadClient: MottakClient,
) {
    @GetMapping("/person")
    fun hentDokumentasjonsbehovForPerson(): ResponseEntity<List<SøknadMedDokumentasjonsbehovDto>> {
        val ident = EksternBrukerUtils.hentFnrFraToken()
        return ResponseEntity.ok(søknadClient.hentSøknaderMedDokumentasjonsbehov(ident))
    }
}
