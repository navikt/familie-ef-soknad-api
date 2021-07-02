package no.nav.familie.ef.søknad.mock

import org.slf4j.Logger
import no.nav.familie.ef.søknad.api.dto.Kvittering
import no.nav.familie.kontrakter.felles.objectMapper
import no.nav.familie.sikkerhet.EksternBrukerUtils
import no.nav.security.token.support.core.api.ProtectedWithClaims
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
@RestController
@ProtectedWithClaims(issuer = EksternBrukerUtils.ISSUER, claimMap = ["acr=Level4"])
class EttersendingController {


    private val log: Logger = LoggerFactory.getLogger(this::class.java)
    private val innsendingMottatt = LocalDateTime.now()

    @PostMapping("/hello")
    fun postEttersending() {
        log.info("hallo")
    }


    /*fun sendInnTest(@RequestBody dokumentasjonsbehov: Map<Any, Any>): Kvittering {
        val valueAsString = objectMapper.writeValueAsString(dokumentasjonsbehov)
        // val readValue : SøknadDto= objectMapper.readValue(valueAsString)
        return Kvittering("Kontakt med api, søknad ikke sendt inn. Du forsøkte å sende inn:  $valueAsString", innsendingMottatt)
    }
    */



}