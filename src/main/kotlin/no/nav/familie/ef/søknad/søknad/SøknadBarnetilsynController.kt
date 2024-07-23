package no.nav.familie.ef.søknad.søknad

import no.nav.familie.ef.søknad.infrastruktur.exception.ApiFeil
import no.nav.familie.ef.søknad.infrastruktur.featuretoggle.FeatureToggleService
import no.nav.familie.ef.søknad.søknad.domain.Kvittering
import no.nav.familie.ef.søknad.søknad.dto.SøknadBarnetilsynDto
import no.nav.familie.ef.søknad.søknad.dto.SøknadBarnetilsynGjenbrukDto
import no.nav.familie.kontrakter.felles.objectMapper
import no.nav.familie.sikkerhet.EksternBrukerUtils
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping(path = ["/api/soknadbarnetilsyn", "/api/soknad/barnetilsyn"], produces = [APPLICATION_JSON_VALUE])
@ProtectedWithClaims(issuer = EksternBrukerUtils.ISSUER_TOKENX, claimMap = ["acr=Level4"])
@Validated
class SøknadBarnetilsynController(
    val søknadService: SøknadService,
    val featureToggleService: FeatureToggleService,
) {
    private val secureLogger = LoggerFactory.getLogger("secureLogger")

    @PostMapping
    fun sendInn(
        @RequestBody søknad: SøknadBarnetilsynDto,
    ): Kvittering {
        if (!EksternBrukerUtils.personIdentErLikInnloggetBruker(søknad.person.søker.fnr)) {
            throw ApiFeil("Fnr fra token matcher ikke fnr på søknaden", HttpStatus.FORBIDDEN)
        }
        val innsendingMottatt = LocalDateTime.now()
        søknadService.sendInn(søknad, innsendingMottatt)
        return Kvittering("ok", mottattDato = innsendingMottatt)
    }

    @GetMapping("forrige")
    fun hentForrigeBarnetilsynSøknad(): SøknadBarnetilsynGjenbrukDto? {
        val forrigeBarnetilsynSøknad = søknadService.hentForrigeBarnetilsynSøknad()
        val personIdent = EksternBrukerUtils.hentFnrFraToken()
        if (forrigeBarnetilsynSøknad != null) {
            secureLogger.info("forrige barnetilsynsøknad for $personIdent : " + objectMapper.writeValueAsString(forrigeBarnetilsynSøknad))
        }
        return forrigeBarnetilsynSøknad
    }
}
