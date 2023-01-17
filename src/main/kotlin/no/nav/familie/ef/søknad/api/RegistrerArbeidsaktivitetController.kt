package no.nav.familie.ef.søknad.api

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.ef.søknad.api.dto.søknadsdialog.Arbeidssøker
import no.nav.familie.ef.søknad.featuretoggle.FeatureToggleService
import no.nav.familie.ef.søknad.mapper.KvitteringMapper
import no.nav.familie.ef.søknad.service.OppslagService
import no.nav.familie.ef.søknad.service.SkjemaService
import no.nav.familie.sikkerhet.EksternBrukerUtils
import no.nav.security.token.support.core.api.Protected
import no.nav.security.token.support.core.api.ProtectedWithClaims
import no.nav.security.token.support.core.api.RequiredIssuers
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping(path = ["/api/registrerarbeid"], produces = [APPLICATION_JSON_VALUE])
@RequiredIssuers(
    ProtectedWithClaims(issuer = EksternBrukerUtils.ISSUER_TOKENX, claimMap = ["acr=Level4"]),
    ProtectedWithClaims(issuer = EksternBrukerUtils.ISSUER_SELVBETJENING, claimMap = ["acr=Level4"])
)
class RegistrerArbeidsaktivitetController(
    val skjemaService: SkjemaService,
    val featureToggleService: FeatureToggleService,
    private val oppslagService: OppslagService
) {

    @PostMapping
    @Protected
    fun sendRegistrerArbeidsAktivitet(@RequestBody arbeidssøker: Arbeidssøker): Kvittering {
        val fnrFraToken = EksternBrukerUtils.hentFnrFraToken()
        val forkortetNavn = oppslagService.hentSøkerNavn()
        val innsendingMottatt = LocalDateTime.now()
        val kvittering = skjemaService.sendInn(arbeidssøker, fnrFraToken, forkortetNavn, innsendingMottatt)
        return KvitteringMapper.mapTilEkstern(kvittering, innsendingMottatt)
    }
}
