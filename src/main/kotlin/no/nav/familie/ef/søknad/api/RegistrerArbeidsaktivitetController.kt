package no.nav.familie.ef.søknad.api

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
import no.nav.familie.ef.søknad.featuretoggle.enabledEllersHttp403
import no.nav.familie.ef.søknad.service.SøknadService
import no.nav.familie.ef.søknad.util.InnloggingUtils
import no.nav.familie.kontrakter.felles.objectMapper
import no.nav.security.token.support.core.api.Protected
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(path = ["/api/registrerarbeid"], produces = [APPLICATION_JSON_VALUE])
@ProtectedWithClaims(issuer = InnloggingUtils.ISSUER, claimMap = ["acr=Level4"])
// @Validated TODO legg på validering minimum. fnr i innsent data = innlogget bruker
class RegistrerArbeidsaktivitetController(val søknadService: SøknadService, val featureToggleService: FeatureToggleService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping
    @Protected
    fun sendRegistrerArbeidsAktivitet(@RequestBody registrering: Map<Any, Any>): Kvittering {
        val valueAsString = objectMapper.writeValueAsString(registrering)
        return featureToggleService.enabledEllersHttp403("familie.ef.soknad.registrerarbeidssoker") {
            Kvittering("Kontakt med api, søknad ikke sendt inn. Du forsøkte å sende inn:  $valueAsString")
        }
    }
}
