package no.nav.familie.ef.søknad.api

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadDto
import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
import no.nav.familie.ef.søknad.featuretoggle.enabledEllersHttp403
import no.nav.familie.ef.søknad.service.SøknadService
import no.nav.familie.ef.søknad.util.InnloggingUtils
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(path = ["/api/soknad"], produces = [APPLICATION_JSON_VALUE])
@ProtectedWithClaims(issuer = InnloggingUtils.ISSUER, claimMap = ["acr=Level4"])

class SøknadController(val søknadService: SøknadService, val featureToggleService: FeatureToggleService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping
    fun sendInn(@RequestBody søknad: Map<Any, Any>): Kvittering {
        return featureToggleService.enabledEllersHttp403("familie.ef.soknad.send-soknad") {
            Kvittering("Kontakt med api, søknad ikke sendt inn. Du forsøkte å sende inn:  $søknad")
        }
    }

    @PostMapping("/v2")
    fun sendInnv2(@RequestBody søknad: SøknadDto): Kvittering {
        logger.info("Send inn v2")
        return featureToggleService.enabledEllersHttp403("familie.ef.soknad.send-soknad") {
            try {
                søknadService.sendInn(søknad)
            } catch (e: Exception) {
                logger.error("Feil - får ikke sendt til mottak ", e)
                Kvittering("Feil! Søknad ikke sendt inn. Du forsøkte å sende inn:  $søknad")
            }
        }
    }
}
