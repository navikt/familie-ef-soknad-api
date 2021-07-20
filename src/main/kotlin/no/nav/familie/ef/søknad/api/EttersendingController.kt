package no.nav.familie.ef.søknad.mock

import no.nav.familie.ef.søknad.api.ApiFeil
import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.EttersendingDto
import no.nav.familie.sikkerhet.EksternBrukerUtils
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.springframework.http.HttpStatus
import no.nav.familie.ef.søknad.service.EttersendingService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping(path = ["/api/ettersending"])
@ProtectedWithClaims(issuer = EksternBrukerUtils.ISSUER, claimMap = ["acr=Level4"])
class EttersendingController(val ettersendingService: EttersendingService) {

    @PostMapping
    fun postEttersending(@RequestBody ettersending: EttersendingDto): Kvittering {
        if (!EksternBrukerUtils.personIdentErLikInnloggetBruker(ettersending.fnr)) {
            throw ApiFeil("Fnr fra token matcher ikke fnr på søknaden", HttpStatus.FORBIDDEN)
        }
        val innsendingMottatt = LocalDateTime.now()
        ettersendingService.sendInn(ettersending, innsendingMottatt)
        return Kvittering("ok", mottattDato = innsendingMottatt)

    }

}