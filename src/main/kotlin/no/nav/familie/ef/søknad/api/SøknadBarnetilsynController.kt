package no.nav.familie.ef.søknad.api

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
import no.nav.familie.ef.søknad.featuretoggle.enabledEllersHttp403
import no.nav.familie.ef.søknad.service.SøknadService
import no.nav.familie.ef.søknad.util.InnloggingUtils
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime


@RestController
@RequestMapping(path = ["/api/soknadbarnetilsyn"], produces = [APPLICATION_JSON_VALUE])
@ProtectedWithClaims(issuer = InnloggingUtils.ISSUER, claimMap = ["acr=Level4"])
@Validated
class SøknadBarnetilsynController(val søknadService: SøknadService, val featureToggleService: FeatureToggleService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    // TODO - Bytt ut String med barnetilsyn når denne er klar
    @PostMapping
    fun sendInn(@RequestBody søknad: String): Kvittering {
        return featureToggleService.enabledEllersHttp403("familie.ef.soknad.api.send-barnetilsynsoknad") {
            try {
                val innsendingMottatt = LocalDateTime.now()
                Kvittering("ok", mottattDato = innsendingMottatt)
            } catch (e: Exception) {
                logger.error("Feil - får ikke sendt barnetilsyn-søknad til mottak ", e)
                error("Noe gikk galt ved innsending")
            }
        }
    }
}
