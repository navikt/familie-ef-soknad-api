package no.nav.familie.ef.søknad.minside

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/saksbehandling")
class SaksbehandlingController(
    private val saksbehandlingService: SaksbehandlingService,
) {
    @GetMapping("/stonadsperioder")
    fun hentStønadsperioderForBruker() = saksbehandlingService.hentStønadsperioderForBruker()

    @GetMapping("/har-overgangsstonad-pa-gammelt-regelverk")
    fun harOvergangsstønadPåGammeltRegelverk() = saksbehandlingService.harOvergangsstønadPåGammeltRegelverk()

    @GetMapping("/har-gyldig-barnetilsyn-ved-regelendring")
    fun harGyldigBarnetilsynVedRegelendring() = saksbehandlingService.harGyldigBarnetilsynVedRegelendring()
}
