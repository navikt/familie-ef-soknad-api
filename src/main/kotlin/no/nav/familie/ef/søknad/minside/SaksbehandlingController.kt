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

    // TODO skal slettes
    @Deprecated("Erstattet av har-overgangsstonad-pa-gammelt-regelverk. Returnerer alltid JA for å sikre gammel flyt for allerede deployede klienter.")
    @GetMapping("/har-vedtak-pa-gammelt-regelverk")
    fun harVedtakPåGammeltRegelverkLegacy() = "JA"

    @GetMapping("/har-gyldig-barnetilsyn-ved-regelendring")
    fun harGyldigBarnetilsynVedRegelendring() = saksbehandlingService.harGyldigBarnetilsynVedRegelendring()
}
