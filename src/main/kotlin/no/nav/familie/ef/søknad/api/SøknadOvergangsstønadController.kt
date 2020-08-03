package no.nav.familie.ef.søknad.api

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.SøknadOvergangsstønadDto
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
@RequestMapping(path = ["/api/soknad"], produces = [APPLICATION_JSON_VALUE])
@ProtectedWithClaims(issuer = InnloggingUtils.ISSUER, claimMap = ["acr=Level4"])
@Validated
class SøknadOvergangsstønadController(val søknadService: SøknadService, val featureToggleService: FeatureToggleService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping
    fun sendInn(@RequestBody @Validated søknad: SøknadOvergangsstønadDto): Kvittering {
        return featureToggleService.enabledEllersHttp403("familie.ef.soknad.send-soknad") {
            try {
                val innsendingMottatt = LocalDateTime.now()
                søknadService.sendInn(søknad, innsendingMottatt)
                Kvittering("ok", mottattDato = innsendingMottatt)
            } catch (e: Exception) {
                logger.error("Feil - får ikke sendt til mottak ", e)
                error("Noe gikk galt ved innsending")
            }
        }
    }
}
