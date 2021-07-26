package no.nav.familie.ef.søknad.api

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.api.dto.ettersending.EttersendingDto
import no.nav.familie.sikkerhet.EksternBrukerUtils
import no.nav.security.token.support.core.api.ProtectedWithClaims
import no.nav.familie.ef.søknad.service.EttersendingService
import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
import no.nav.familie.ef.søknad.featuretoggle.enabledEllersHttp403
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping(path = ["/api/ettersending"])
@ProtectedWithClaims(issuer = EksternBrukerUtils.ISSUER, claimMap = ["acr=Level4"])

class EttersendingController(val ettersendingService: EttersendingService,
                             val featureToggleService: FeatureToggleService) {

    @PostMapping
    fun postEttersending(@RequestBody ettersending: EttersendingDto): Kvittering {
        return featureToggleService.enabledEllersHttp403("familie.ef.soknad.api.ettersending") {
            if (!EksternBrukerUtils.personIdentErLikInnloggetBruker(ettersending.fnr)) {
                throw ApiFeil("Fnr fra token matcher ikke fnr på søknaden", HttpStatus.FORBIDDEN)
            }
            val innsendingMottatt = LocalDateTime.now()
            ettersendingService.sendInn(ettersending, innsendingMottatt)
            Kvittering("ok", mottattDato = innsendingMottatt)

        }
    }

}