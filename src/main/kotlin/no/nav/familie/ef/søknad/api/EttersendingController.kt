package no.nav.familie.ef.søknad.api

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
import no.nav.familie.ef.søknad.service.EttersendingService
import no.nav.familie.kontrakter.ef.ettersending.EttersendelseDto
import no.nav.familie.sikkerhet.EksternBrukerUtils
import no.nav.security.token.support.core.api.ProtectedWithClaims
import no.nav.security.token.support.core.api.RequiredIssuers
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping(path = ["/api/ettersending"])
@RequiredIssuers(
    ProtectedWithClaims(issuer = "idporten", claimMap = ["acr=Level4"]),
    ProtectedWithClaims(issuer = "selvbetjening", claimMap = ["acr=Level4"])
)
class EttersendingController(
    val ettersendingService: EttersendingService,
    val featureToggleService: FeatureToggleService
) {

    @PostMapping
    fun postEttersending(@RequestBody ettersending: EttersendelseDto): Kvittering {
        if (!EksternBrukerUtils.personIdentErLikInnloggetBruker(ettersending.personIdent)) {
            throw ApiFeil("Fnr fra token matcher ikke fnr på søknaden", HttpStatus.FORBIDDEN)
        }
        val innsendingMottatt = LocalDateTime.now()
        ettersendingService.sendInn(ettersending, innsendingMottatt)
        return Kvittering("ok", mottattDato = innsendingMottatt)
    }

    @GetMapping
    fun hentEttersendingForPerson(): ResponseEntity<List<EttersendelseDto>> {
        val ident = EksternBrukerUtils.hentFnrFraToken()
        if (!EksternBrukerUtils.personIdentErLikInnloggetBruker(ident)) {
            throw ApiFeil("Fnr fra token matcher ikke fnr på søknaden", HttpStatus.FORBIDDEN)
        }
        return ResponseEntity.ok(ettersendingService.hentEttersendingForPerson(ident))
    }
}
