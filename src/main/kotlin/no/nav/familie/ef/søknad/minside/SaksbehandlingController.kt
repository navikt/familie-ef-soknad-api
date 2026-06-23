package no.nav.familie.ef.søknad.minside

import no.nav.familie.sikkerhet.EksternBrukerUtils
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/saksbehandling")
@ProtectedWithClaims(issuer = EksternBrukerUtils.ISSUER_TOKENX, claimMap = ["acr=Level4"])
class SaksbehandlingController(
    private val saksbehandlingService: SaksbehandlingService,
) {
    @GetMapping("/stonadsperioder")
    fun hentStønadsperioderForBruker() = saksbehandlingService.hentStønadsperioderForBruker()

    @GetMapping("/har-vedtak-pa-gammelt-regelverk")
    fun harVedtakPåGammeltRegelverk() = saksbehandlingService.harVedtakPåGammeltRegelverk()

    @GetMapping("/har-gyldig-barnetilsyn-ved-regelendring")
    fun harGyldigBarnetilsynVedRegelendring() = saksbehandlingService.harGyldigBarnetilsynVedRegelendring()
}
