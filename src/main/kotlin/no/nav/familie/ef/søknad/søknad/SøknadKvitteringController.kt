package no.nav.familie.ef.søknad.søknad

import no.nav.familie.ef.søknad.infrastruktur.exception.ApiFeil
import no.nav.familie.ef.søknad.person.OppslagService
import no.nav.familie.ef.søknad.søknad.domain.Arbeidssøker
import no.nav.familie.ef.søknad.søknad.domain.Kvittering
import no.nav.familie.ef.søknad.søknad.dto.SøknadBarnetilsynDto
import no.nav.familie.ef.søknad.søknad.dto.SøknadBarnetilsynGjenbrukDto
import no.nav.familie.ef.søknad.søknad.dto.SøknadOvergangsstønadDto
import no.nav.familie.ef.søknad.søknad.dto.SøknadSkolepengerDto
import no.nav.familie.sikkerhet.EksternBrukerUtils
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

// TODO: Endre endepunkt til /api/soknad etter at de andre søknadscontrollerne er utfaset
@RestController
@RequestMapping(path = ["/api/soknadskvittering"], produces = [MediaType.APPLICATION_JSON_VALUE])
@ProtectedWithClaims(issuer = EksternBrukerUtils.ISSUER_TOKENX, claimMap = ["acr=Level4"])
@Validated
class SøknadKvitteringController(
    val søknadService: SøknadService,
    private val oppslagService: OppslagService,
) {
    @PostMapping("overgangsstonad")
    fun sendInn(
        @RequestBody søknad: SøknadOvergangsstønadDto,
    ): Kvittering {
        if (!EksternBrukerUtils.personIdentErLikInnloggetBruker(søknad.person.søker.fnr)) {
            throw ApiFeil("Fnr fra token matcher ikke fnr på søknaden", HttpStatus.FORBIDDEN)
        }

        val innsendingMottatt = LocalDateTime.now()
        søknadService.sendInnSøknadskvittering(søknad, innsendingMottatt)
        return Kvittering("ok", mottattDato = innsendingMottatt)
    }

    @PostMapping("barnetilsyn")
    fun sendInn(
        @RequestBody søknad: SøknadBarnetilsynDto,
    ): Kvittering {
        if (!EksternBrukerUtils.personIdentErLikInnloggetBruker(søknad.person.søker.fnr)) {
            throw ApiFeil("Fnr fra token matcher ikke fnr på søknaden", HttpStatus.FORBIDDEN)
        }
        val innsendingMottatt = LocalDateTime.now()
        søknadService.sendInnSøknadskvitteringBarnetilsyn(søknad, innsendingMottatt)
        return Kvittering("ok", mottattDato = innsendingMottatt)
    }

    @GetMapping("barnetilsyn/forrige")
    fun hentForrigeBarnetilsynSøknad(): SøknadBarnetilsynGjenbrukDto? = søknadService.hentForrigeBarnetilsynSøknadKvittering()

    @PostMapping("skolepenger")
    fun sendInn(
        @RequestBody søknad: SøknadSkolepengerDto,
    ): Kvittering {
        if (!EksternBrukerUtils.personIdentErLikInnloggetBruker(søknad.person.søker.fnr)) {
            throw ApiFeil("Fnr fra token matcher ikke fnr på søknaden", HttpStatus.FORBIDDEN)
        }
        val innsendingMottatt = LocalDateTime.now()
        søknadService.sendInnSøknadskvitteringSkolepenger(søknad, innsendingMottatt)
        return Kvittering("ok", mottattDato = innsendingMottatt)
    }

    @PostMapping("arbeidssoker")
    fun sendInn(
        @RequestBody arbeidssøker: Arbeidssøker,
    ): Kvittering {
        val fnrFraToken = EksternBrukerUtils.hentFnrFraToken()
        val forkortetNavn = oppslagService.hentSøkerNavn()
        val innsendingMottatt = LocalDateTime.now()
        søknadService.sendInnSøknadskvitteringArbeidssøker(arbeidssøker, fnrFraToken, forkortetNavn, innsendingMottatt)
        return Kvittering("ok", mottattDato = innsendingMottatt)
    }

    @GetMapping("sist-innsendt-per-stonad")
    fun hentSistInnsendteSøknadPerStønad() = søknadService.hentSistInnsendtSøknadPerStønad()

    @Profile("!prod")
    @GetMapping("{søknadId}")
    fun hentSøknad(
        @PathVariable søknadId: String,
    ): ByteArray = søknadService.hentSøknadPdf(søknadId)
}
