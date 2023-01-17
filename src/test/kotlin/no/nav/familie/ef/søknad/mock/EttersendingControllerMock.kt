package no.nav.familie.ef.søknad.mock

import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.kontrakter.felles.objectMapper
import no.nav.familie.sikkerhet.EksternBrukerUtils
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@ProtectedWithClaims(issuer = EksternBrukerUtils.ISSUER_TOKENX, claimMap = ["acr=Level4"])
class EttersendingControllerMock {

    private val innsendingMottatt = LocalDateTime.now()

    @PostMapping("/api/ettersending/test")
    fun postEttersending(@RequestBody msgBody: Map<Any, Any>): Kvittering {
        val valueAsString = objectMapper.writeValueAsString((msgBody))
        return Kvittering("Kontakt med api, ettersending er ikke sendt inn. Du forsøkte å sende inn:  $valueAsString", innsendingMottatt)
    }
}
