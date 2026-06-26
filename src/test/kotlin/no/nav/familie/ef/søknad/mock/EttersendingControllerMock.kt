package no.nav.familie.ef.søknad.mock

import no.nav.familie.ef.søknad.søknad.domain.Kvittering
import no.nav.familie.kontrakter.felles.jsonMapper
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class EttersendingControllerMock {
    private val innsendingMottatt = LocalDateTime.now()

    @PostMapping("/api/ettersending/test")
    fun postEttersending(
        @RequestBody msgBody: Map<Any, Any>,
    ): Kvittering {
        val valueAsString = jsonMapper.writeValueAsString((msgBody))
        return Kvittering("Kontakt med api, ettersending er ikke sendt inn. Du forsøkte å sende inn:  $valueAsString", innsendingMottatt)
    }
}
