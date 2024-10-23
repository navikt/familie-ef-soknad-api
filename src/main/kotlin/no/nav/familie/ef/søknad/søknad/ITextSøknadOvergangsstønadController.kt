package no.nav.familie.ef.søknad.søknad

import no.nav.familie.ef.søknad.infrastruktur.exception.ApiFeil
import no.nav.familie.ef.søknad.søknad.domain.Kvittering
import no.nav.familie.ef.søknad.søknad.dto.SøknadOvergangsstønadDto
import no.nav.familie.sikkerhet.EksternBrukerUtils
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@Profile("!prod")
@RestController
@RequestMapping(path = ["/api/itext-soknad", "/api/itext-soknad/overgangsstonad"], produces = [MediaType.APPLICATION_JSON_VALUE])
@ProtectedWithClaims(issuer = EksternBrukerUtils.ISSUER_TOKENX, claimMap = ["acr=Level4"])
@Validated
class ITextSøknadOvergangsstønadController(
    val søknadService: SøknadService,
) {
    @PostMapping
    fun sendInn(
        @RequestBody søknad: SøknadOvergangsstønadDto,
    ): Kvittering {
        if (!EksternBrukerUtils.personIdentErLikInnloggetBruker(søknad.person.søker.fnr)) {
            throw ApiFeil("Fnr fra token matcher ikke fnr på søknaden", HttpStatus.FORBIDDEN)
        }

        val innsendingMottatt = LocalDateTime.now()
        søknadService.sendInn(søknad, innsendingMottatt)
        return Kvittering("ok", mottattDato = innsendingMottatt)
    }
}