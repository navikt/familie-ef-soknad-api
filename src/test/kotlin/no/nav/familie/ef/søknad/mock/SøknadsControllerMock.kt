package no.nav.familie.ef.søknad.mock

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
import no.nav.familie.ef.søknad.featuretoggle.enabledEllersHttp403
import no.nav.familie.ef.søknad.util.InnloggingUtils
import no.nav.familie.kontrakter.felles.objectMapper
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api/soknad/test"], produces = [MediaType.APPLICATION_JSON_VALUE])
@ProtectedWithClaims(issuer = InnloggingUtils.ISSUER, claimMap = ["acr=Level4"])
class SøknadsControllerMock(val featureToggleService: FeatureToggleService) {

    @PostMapping
    fun sendInnTest(@RequestBody søknad: Map<Any, Any>): Kvittering {
        val valueAsString = objectMapper.writeValueAsString(søknad)
        // val readValue : SøknadDto= objectMapper.readValue(valueAsString)
        return featureToggleService.enabledEllersHttp403("familie.ef.soknad.send-soknad") {
            Kvittering("Kontakt med api, søknad ikke sendt inn. Du forsøkte å sende inn:  $valueAsString")
        }
    }

}
